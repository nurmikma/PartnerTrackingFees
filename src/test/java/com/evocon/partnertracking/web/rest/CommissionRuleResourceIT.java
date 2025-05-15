package com.evocon.partnertracking.web.rest;

import static com.evocon.partnertracking.domain.CommissionRuleAsserts.*;
import static com.evocon.partnertracking.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evocon.partnertracking.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evocon.partnertracking.IntegrationTest;
import com.evocon.partnertracking.domain.CommissionRule;
import com.evocon.partnertracking.repository.CommissionRuleRepository;
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
 * Integration tests for the {@link CommissionRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CommissionRuleResourceIT {

    private static final String DEFAULT_RULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RULE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_START_DAY = 1;
    private static final Integer UPDATED_START_DAY = 2;

    private static final Integer DEFAULT_END_DAY = 1;
    private static final Integer UPDATED_END_DAY = 2;

    private static final BigDecimal DEFAULT_COMMISSION_PERCENTAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMISSION_PERCENTAGE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/commission-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CommissionRuleRepository commissionRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCommissionRuleMockMvc;

    private CommissionRule commissionRule;

    private CommissionRule insertedCommissionRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRule createEntity() {
        return new CommissionRule()
            .ruleName(DEFAULT_RULE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .startDay(DEFAULT_START_DAY)
            .endDay(DEFAULT_END_DAY)
            .commissionPercentage(DEFAULT_COMMISSION_PERCENTAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CommissionRule createUpdatedEntity() {
        return new CommissionRule()
            .ruleName(UPDATED_RULE_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDay(UPDATED_START_DAY)
            .endDay(UPDATED_END_DAY)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE);
    }

    @BeforeEach
    void initTest() {
        commissionRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCommissionRule != null) {
            commissionRuleRepository.delete(insertedCommissionRule);
            insertedCommissionRule = null;
        }
    }

    @Test
    @Transactional
    void createCommissionRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CommissionRule
        var returnedCommissionRule = om.readValue(
            restCommissionRuleMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CommissionRule.class
        );

        // Validate the CommissionRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCommissionRuleUpdatableFieldsEquals(returnedCommissionRule, getPersistedCommissionRule(returnedCommissionRule));

        insertedCommissionRule = returnedCommissionRule;
    }

    @Test
    @Transactional
    void createCommissionRuleWithExistingId() throws Exception {
        // Create the CommissionRule with an existing ID
        commissionRule.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCommissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRuleNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setRuleName(null);

        // Create the CommissionRule, which fails.

        restCommissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setDescription(null);

        // Create the CommissionRule, which fails.

        restCommissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setStartDay(null);

        // Create the CommissionRule, which fails.

        restCommissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCommissionPercentageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        commissionRule.setCommissionPercentage(null);

        // Create the CommissionRule, which fails.

        restCommissionRuleMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCommissionRules() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        // Get all the commissionRuleList
        restCommissionRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(commissionRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleName").value(hasItem(DEFAULT_RULE_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(DEFAULT_START_DAY)))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(DEFAULT_END_DAY)))
            .andExpect(jsonPath("$.[*].commissionPercentage").value(hasItem(sameNumber(DEFAULT_COMMISSION_PERCENTAGE))));
    }

    @Test
    @Transactional
    void getCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        // Get the commissionRule
        restCommissionRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, commissionRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(commissionRule.getId().intValue()))
            .andExpect(jsonPath("$.ruleName").value(DEFAULT_RULE_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDay").value(DEFAULT_START_DAY))
            .andExpect(jsonPath("$.endDay").value(DEFAULT_END_DAY))
            .andExpect(jsonPath("$.commissionPercentage").value(sameNumber(DEFAULT_COMMISSION_PERCENTAGE)));
    }

    @Test
    @Transactional
    void getNonExistingCommissionRule() throws Exception {
        // Get the commissionRule
        restCommissionRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule
        CommissionRule updatedCommissionRule = commissionRuleRepository.findById(commissionRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCommissionRule are not directly saved in db
        em.detach(updatedCommissionRule);
        updatedCommissionRule
            .ruleName(UPDATED_RULE_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDay(UPDATED_START_DAY)
            .endDay(UPDATED_END_DAY)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE);

        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCommissionRule.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCommissionRule))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCommissionRuleToMatchAllProperties(updatedCommissionRule);
    }

    @Test
    @Transactional
    void putNonExistingCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, commissionRule.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(commissionRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCommissionRuleWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule using partial update
        CommissionRule partialUpdatedCommissionRule = new CommissionRule();
        partialUpdatedCommissionRule.setId(commissionRule.getId());

        partialUpdatedCommissionRule
            .ruleName(UPDATED_RULE_NAME)
            .startDay(UPDATED_START_DAY)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE);

        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRule))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCommissionRule, commissionRule),
            getPersistedCommissionRule(commissionRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateCommissionRuleWithPatch() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the commissionRule using partial update
        CommissionRule partialUpdatedCommissionRule = new CommissionRule();
        partialUpdatedCommissionRule.setId(commissionRule.getId());

        partialUpdatedCommissionRule
            .ruleName(UPDATED_RULE_NAME)
            .description(UPDATED_DESCRIPTION)
            .startDay(UPDATED_START_DAY)
            .endDay(UPDATED_END_DAY)
            .commissionPercentage(UPDATED_COMMISSION_PERCENTAGE);

        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCommissionRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCommissionRule))
            )
            .andExpect(status().isOk());

        // Validate the CommissionRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCommissionRuleUpdatableFieldsEquals(partialUpdatedCommissionRule, getPersistedCommissionRule(partialUpdatedCommissionRule));
    }

    @Test
    @Transactional
    void patchNonExistingCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, commissionRule.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCommissionRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        commissionRule.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCommissionRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(commissionRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CommissionRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCommissionRule() throws Exception {
        // Initialize the database
        insertedCommissionRule = commissionRuleRepository.saveAndFlush(commissionRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the commissionRule
        restCommissionRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, commissionRule.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return commissionRuleRepository.count();
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

    protected CommissionRule getPersistedCommissionRule(CommissionRule commissionRule) {
        return commissionRuleRepository.findById(commissionRule.getId()).orElseThrow();
    }

    protected void assertPersistedCommissionRuleToMatchAllProperties(CommissionRule expectedCommissionRule) {
        assertCommissionRuleAllPropertiesEquals(expectedCommissionRule, getPersistedCommissionRule(expectedCommissionRule));
    }

    protected void assertPersistedCommissionRuleToMatchUpdatableProperties(CommissionRule expectedCommissionRule) {
        assertCommissionRuleAllUpdatablePropertiesEquals(expectedCommissionRule, getPersistedCommissionRule(expectedCommissionRule));
    }
}
