package com.evocon.partnertracking.web.rest;

import static com.evocon.partnertracking.domain.CommissionRuleSetAsserts.*;
import static com.evocon.partnertracking.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evocon.partnertracking.IntegrationTest;
import com.evocon.partnertracking.domain.CommissionRuleSet;
import com.evocon.partnertracking.repository.CommissionRuleSetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CommissionRuleSetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommissionRuleSetResourceIT {

    private static final String DEFAULT_RULE_SET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RULE_SET_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/commission-rule-sets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommissionRuleSetRepository commissionRuleSetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommissionRuleSetMockMvc;

    private CommissionRuleSet commissionRuleSet;

    private CommissionRuleSet insertedCommissionRuleSet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRuleSet createEntity() {
        return new CommissionRuleSet().ruleSetName(DEFAULT_RULE_SET_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRuleSet createUpdatedEntity() {
        return new CommissionRuleSet().ruleSetName(UPDATED_RULE_SET_NAME);
    }

    @BeforeEach
    void initTest() {
        commissionRuleSet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommissionRuleSet != null) {
            commissionRuleSetRepository.delete(insertedCommissionRuleSet);
            insertedCommissionRuleSet = null;
        }
    }

    @Test
    @Transactional
    void createCommissionRuleSet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommissionRuleSet
        var returnedCommissionRuleSet = om.readValue(
            restCommissionRuleSetMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(commissionRuleSet))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommissionRuleSet.class
        );

        // Validate the CommissionRuleSet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommissionRuleSetUpdatableFieldsEquals(returnedCommissionRuleSet, getPersistedCommissionRuleSet(returnedCommissionRuleSet));

        insertedCommissionRuleSet = returnedCommissionRuleSet;
    }

    @Test
    @Transactional
    void createCommissionRuleSetWithExistingId() throws Exception {
        // Create the CommissionRuleSet with an existing ID
        commissionRuleSet.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommissionRuleSetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRuleSetNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRuleSet.setRuleSetName(null);

        // Create the CommissionRuleSet, which fails.

        restCommissionRuleSetMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommissionRuleSets() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        // Get all the commissionRuleSetList
        restCommissionRuleSetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commissionRuleSet.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleSetName").value(hasItem(DEFAULT_RULE_SET_NAME)));
    }

    @Test
    @Transactional
    void getCommissionRuleSet() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        // Get the commissionRuleSet
        restCommissionRuleSetMockMvc
            .perform(get(ENTITY_API_URL_ID, commissionRuleSet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commissionRuleSet.getId().intValue()))
            .andExpect(jsonPath("$.ruleSetName").value(DEFAULT_RULE_SET_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCommissionRuleSet() throws Exception {
        // Get the commissionRuleSet
        restCommissionRuleSetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommissionRuleSet() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRuleSet
        CommissionRuleSet updatedCommissionRuleSet = commissionRuleSetRepository.findById(commissionRuleSet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommissionRuleSet are not directly saved in db
        em.detach(updatedCommissionRuleSet);
        updatedCommissionRuleSet.ruleSetName(UPDATED_RULE_SET_NAME);

        restCommissionRuleSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommissionRuleSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommissionRuleSet))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommissionRuleSetToMatchAllProperties(updatedCommissionRuleSet);
    }

    @Test
    @Transactional
    void putNonExistingCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commissionRuleSet.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommissionRuleSetWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRuleSet using partial update
        CommissionRuleSet partialUpdatedCommissionRuleSet = new CommissionRuleSet();
        partialUpdatedCommissionRuleSet.setId(commissionRuleSet.getId());

        restCommissionRuleSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRuleSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRuleSet))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRuleSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleSetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommissionRuleSet, commissionRuleSet),
            getPersistedCommissionRuleSet(commissionRuleSet)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommissionRuleSetWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRuleSet using partial update
        CommissionRuleSet partialUpdatedCommissionRuleSet = new CommissionRuleSet();
        partialUpdatedCommissionRuleSet.setId(commissionRuleSet.getId());

        partialUpdatedCommissionRuleSet.ruleSetName(UPDATED_RULE_SET_NAME);

        restCommissionRuleSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRuleSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRuleSet))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRuleSet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleSetUpdatableFieldsEquals(
            partialUpdatedCommissionRuleSet,
            getPersistedCommissionRuleSet(partialUpdatedCommissionRuleSet)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commissionRuleSet.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommissionRuleSet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRuleSet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleSetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRuleSet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRuleSet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommissionRuleSet() throws Exception {
        // Initialize the database
        insertedCommissionRuleSet = commissionRuleSetRepository.saveAndFlush(commissionRuleSet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commissionRuleSet
        restCommissionRuleSetMockMvc
            .perform(delete(ENTITY_API_URL_ID, commissionRuleSet.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commissionRuleSetRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CommissionRuleSet getPersistedCommissionRuleSet(CommissionRuleSet commissionRuleSet) {
        return commissionRuleSetRepository.findById(commissionRuleSet.getId()).orElseThrow();
    }

    protected void assertPersistedCommissionRuleSetToMatchAllProperties(CommissionRuleSet expectedCommissionRuleSet) {
        assertCommissionRuleSetAllPropertiesEquals(expectedCommissionRuleSet, getPersistedCommissionRuleSet(expectedCommissionRuleSet));
    }

    protected void assertPersistedCommissionRuleSetToMatchUpdatableProperties(CommissionRuleSet expectedCommissionRuleSet) {
        assertCommissionRuleSetAllUpdatablePropertiesEquals(
            expectedCommissionRuleSet,
            getPersistedCommissionRuleSet(expectedCommissionRuleSet)
        );
    }
}
