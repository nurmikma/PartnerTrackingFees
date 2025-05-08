package com.evocon.partnertracking.web.rest;

import static com.evocon.partnertracking.domain.CommissionFeeAsserts.*;
import static com.evocon.partnertracking.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evocon.partnertracking.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evocon.partnertracking.IntegrationTest;
import com.evocon.partnertracking.domain.CommissionFee;
import com.evocon.partnertracking.repository.CommissionFeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CommissionFeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommissionFeeResourceIT {

    private static final BigDecimal DEFAULT_COMMISSION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISSION_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/commission-fees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommissionFeeRepository commissionFeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommissionFeeMockMvc;

    private CommissionFee commissionFee;

    private CommissionFee insertedCommissionFee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionFee createEntity() {
        return new CommissionFee().commissionAmount(DEFAULT_COMMISSION_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionFee createUpdatedEntity() {
        return new CommissionFee().commissionAmount(UPDATED_COMMISSION_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        commissionFee = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommissionFee != null) {
            commissionFeeRepository.delete(insertedCommissionFee);
            insertedCommissionFee = null;
        }
    }

    @Test
    @Transactional
    void createCommissionFee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommissionFee
        var returnedCommissionFee = om.readValue(
            restCommissionFeeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionFee))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommissionFee.class
        );

        // Validate the CommissionFee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommissionFeeUpdatableFieldsEquals(returnedCommissionFee, getPersistedCommissionFee(returnedCommissionFee));

        insertedCommissionFee = returnedCommissionFee;
    }

    @Test
    @Transactional
    void createCommissionFeeWithExistingId() throws Exception {
        // Create the CommissionFee with an existing ID
        commissionFee.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommissionFeeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionFee)))
            .andExpect(status().isBadRequest());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCommissionAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionFee.setCommissionAmount(null);

        // Create the CommissionFee, which fails.

        restCommissionFeeMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionFee)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommissionFees() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        // Get all the commissionFeeList
        restCommissionFeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commissionFee.getId().intValue())))
            .andExpect(jsonPath("$.[*].commissionAmount").value(hasItem(sameNumber(DEFAULT_COMMISSION_AMOUNT))));
    }

    @Test
    @Transactional
    void getCommissionFee() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        // Get the commissionFee
        restCommissionFeeMockMvc
            .perform(get(ENTITY_API_URL_ID, commissionFee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commissionFee.getId().intValue()))
            .andExpect(jsonPath("$.commissionAmount").value(sameNumber(DEFAULT_COMMISSION_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingCommissionFee() throws Exception {
        // Get the commissionFee
        restCommissionFeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommissionFee() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionFee
        CommissionFee updatedCommissionFee = commissionFeeRepository.findById(commissionFee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommissionFee are not directly saved in db
        em.detach(updatedCommissionFee);
        updatedCommissionFee.commissionAmount(UPDATED_COMMISSION_AMOUNT);

        restCommissionFeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommissionFee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommissionFee))
            )
            .andExpect(status().isOk());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommissionFeeToMatchAllProperties(updatedCommissionFee);
    }

    @Test
    @Transactional
    void putNonExistingCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commissionFee.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionFee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionFee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionFee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommissionFeeWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionFee using partial update
        CommissionFee partialUpdatedCommissionFee = new CommissionFee();
        partialUpdatedCommissionFee.setId(commissionFee.getId());

        restCommissionFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionFee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionFee))
            )
            .andExpect(status().isOk());

        // Validate the CommissionFee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionFeeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommissionFee, commissionFee),
            getPersistedCommissionFee(commissionFee)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommissionFeeWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionFee using partial update
        CommissionFee partialUpdatedCommissionFee = new CommissionFee();
        partialUpdatedCommissionFee.setId(commissionFee.getId());

        partialUpdatedCommissionFee.commissionAmount(UPDATED_COMMISSION_AMOUNT);

        restCommissionFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionFee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionFee))
            )
            .andExpect(status().isOk());

        // Validate the CommissionFee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionFeeUpdatableFieldsEquals(partialUpdatedCommissionFee, getPersistedCommissionFee(partialUpdatedCommissionFee));
    }

    @Test
    @Transactional
    void patchNonExistingCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commissionFee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionFee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionFee))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommissionFee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionFee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionFeeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commissionFee))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionFee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommissionFee() throws Exception {
        // Initialize the database
        insertedCommissionFee = commissionFeeRepository.saveAndFlush(commissionFee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commissionFee
        restCommissionFeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, commissionFee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commissionFeeRepository.count();
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

    protected CommissionFee getPersistedCommissionFee(CommissionFee commissionFee) {
        return commissionFeeRepository.findById(commissionFee.getId()).orElseThrow();
    }

    protected void assertPersistedCommissionFeeToMatchAllProperties(CommissionFee expectedCommissionFee) {
        assertCommissionFeeAllPropertiesEquals(expectedCommissionFee, getPersistedCommissionFee(expectedCommissionFee));
    }

    protected void assertPersistedCommissionFeeToMatchUpdatableProperties(CommissionFee expectedCommissionFee) {
        assertCommissionFeeAllUpdatablePropertiesEquals(expectedCommissionFee, getPersistedCommissionFee(expectedCommissionFee));
    }
}
