package com.evocon.partnertracking.web.rest;

import static com.evocon.partnertracking.domain.LicenseAsserts.*;
import static com.evocon.partnertracking.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evocon.partnertracking.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evocon.partnertracking.IntegrationTest;
import com.evocon.partnertracking.domain.License;
import com.evocon.partnertracking.repository.LicenseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link LicenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LicenseResourceIT {

    private static final String DEFAULT_LICENSE_RULE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LICENSE_RULE_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LICENSE_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LICENSE_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LICENSE_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LICENSE_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_LICENSE_QUANTITY = 1;
    private static final Integer UPDATED_LICENSE_QUANTITY = 2;

    private static final BigDecimal DEFAULT_PRICE_PER_LICENSE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_LICENSE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_LICENSE_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_LICENSE_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/licenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LicenseRepository licenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLicenseMockMvc;

    private License license;

    private License insertedLicense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createEntity() {
        return new License()
            .licenseRuleName(DEFAULT_LICENSE_RULE_NAME)
            .licenseStartDate(DEFAULT_LICENSE_START_DATE)
            .licenseEndDate(DEFAULT_LICENSE_END_DATE)
            .licenseQuantity(DEFAULT_LICENSE_QUANTITY)
            .pricePerLicense(DEFAULT_PRICE_PER_LICENSE)
            .totalLicenseAmount(DEFAULT_TOTAL_LICENSE_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static License createUpdatedEntity() {
        return new License()
            .licenseRuleName(UPDATED_LICENSE_RULE_NAME)
            .licenseStartDate(UPDATED_LICENSE_START_DATE)
            .licenseEndDate(UPDATED_LICENSE_END_DATE)
            .licenseQuantity(UPDATED_LICENSE_QUANTITY)
            .pricePerLicense(UPDATED_PRICE_PER_LICENSE)
            .totalLicenseAmount(UPDATED_TOTAL_LICENSE_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        license = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLicense != null) {
            licenseRepository.delete(insertedLicense);
            insertedLicense = null;
        }
    }

    @Test
    @Transactional
    void createLicense() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the License
        var returnedLicense = om.readValue(
            restLicenseMockMvc
                .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            License.class
        );

        // Validate the License in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLicenseUpdatableFieldsEquals(returnedLicense, getPersistedLicense(returnedLicense));

        insertedLicense = returnedLicense;
    }

    @Test
    @Transactional
    void createLicenseWithExistingId() throws Exception {
        // Create the License with an existing ID
        license.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLicenseMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLicenseRuleNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        license.setLicenseRuleName(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        license.setLicenseStartDate(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLicenseQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        license.setLicenseQuantity(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPricePerLicenseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        license.setPricePerLicense(null);

        // Create the License, which fails.

        restLicenseMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLicenses() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        // Get all the licenseList
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(license.getId().intValue())))
            .andExpect(jsonPath("$.[*].licenseRuleName").value(hasItem(DEFAULT_LICENSE_RULE_NAME)))
            .andExpect(jsonPath("$.[*].licenseStartDate").value(hasItem(DEFAULT_LICENSE_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].licenseEndDate").value(hasItem(DEFAULT_LICENSE_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].licenseQuantity").value(hasItem(DEFAULT_LICENSE_QUANTITY)))
            .andExpect(jsonPath("$.[*].pricePerLicense").value(hasItem(sameNumber(DEFAULT_PRICE_PER_LICENSE))))
            .andExpect(jsonPath("$.[*].totalLicenseAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_LICENSE_AMOUNT))));
    }

    @Test
    @Transactional
    void getLicense() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        // Get the license
        restLicenseMockMvc
            .perform(get(ENTITY_API_URL_ID, license.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(license.getId().intValue()))
            .andExpect(jsonPath("$.licenseRuleName").value(DEFAULT_LICENSE_RULE_NAME))
            .andExpect(jsonPath("$.licenseStartDate").value(DEFAULT_LICENSE_START_DATE.toString()))
            .andExpect(jsonPath("$.licenseEndDate").value(DEFAULT_LICENSE_END_DATE.toString()))
            .andExpect(jsonPath("$.licenseQuantity").value(DEFAULT_LICENSE_QUANTITY))
            .andExpect(jsonPath("$.pricePerLicense").value(sameNumber(DEFAULT_PRICE_PER_LICENSE)))
            .andExpect(jsonPath("$.totalLicenseAmount").value(sameNumber(DEFAULT_TOTAL_LICENSE_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingLicense() throws Exception {
        // Get the license
        restLicenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLicense() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the license
        License updatedLicense = licenseRepository.findById(license.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLicense are not directly saved in db
        em.detach(updatedLicense);
        updatedLicense
            .licenseRuleName(UPDATED_LICENSE_RULE_NAME)
            .licenseStartDate(UPDATED_LICENSE_START_DATE)
            .licenseEndDate(UPDATED_LICENSE_END_DATE)
            .licenseQuantity(UPDATED_LICENSE_QUANTITY)
            .pricePerLicense(UPDATED_PRICE_PER_LICENSE)
            .totalLicenseAmount(UPDATED_TOTAL_LICENSE_AMOUNT);

        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLicense.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLicenseToMatchAllProperties(updatedLicense);
    }

    @Test
    @Transactional
    void putNonExistingLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, license.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(license)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense.pricePerLicense(UPDATED_PRICE_PER_LICENSE).totalLicenseAmount(UPDATED_TOTAL_LICENSE_AMOUNT);

        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLicenseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLicense, license), getPersistedLicense(license));
    }

    @Test
    @Transactional
    void fullUpdateLicenseWithPatch() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the license using partial update
        License partialUpdatedLicense = new License();
        partialUpdatedLicense.setId(license.getId());

        partialUpdatedLicense
            .licenseRuleName(UPDATED_LICENSE_RULE_NAME)
            .licenseStartDate(UPDATED_LICENSE_START_DATE)
            .licenseEndDate(UPDATED_LICENSE_END_DATE)
            .licenseQuantity(UPDATED_LICENSE_QUANTITY)
            .pricePerLicense(UPDATED_PRICE_PER_LICENSE)
            .totalLicenseAmount(UPDATED_TOTAL_LICENSE_AMOUNT);

        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLicense.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLicense))
            )
            .andExpect(status().isOk());

        // Validate the License in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLicenseUpdatableFieldsEquals(partialUpdatedLicense, getPersistedLicense(partialUpdatedLicense));
    }

    @Test
    @Transactional
    void patchNonExistingLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, license.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(license))
            )
            .andExpect(status().isBadRequest());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLicense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        license.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLicenseMockMvc
            .perform(patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(license)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the License in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLicense() throws Exception {
        // Initialize the database
        insertedLicense = licenseRepository.saveAndFlush(license);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the license
        restLicenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, license.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return licenseRepository.count();
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

    protected License getPersistedLicense(License license) {
        return licenseRepository.findById(license.getId()).orElseThrow();
    }

    protected void assertPersistedLicenseToMatchAllProperties(License expectedLicense) {
        assertLicenseAllPropertiesEquals(expectedLicense, getPersistedLicense(expectedLicense));
    }

    protected void assertPersistedLicenseToMatchUpdatableProperties(License expectedLicense) {
        assertLicenseAllUpdatablePropertiesEquals(expectedLicense, getPersistedLicense(expectedLicense));
    }
}
