package com.evocon.partnertracking.web.rest;

import static com.evocon.partnertracking.domain.InvoiceLineAsserts.*;
import static com.evocon.partnertracking.web.rest.TestUtil.createUpdateProxyForBean;
import static com.evocon.partnertracking.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.evocon.partnertracking.IntegrationTest;
import com.evocon.partnertracking.domain.InvoiceLine;
import com.evocon.partnertracking.repository.InvoiceLineRepository;
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
 * Integration tests for the {@link InvoiceLineResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceLineResourceIT {

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/invoice-lines";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvoiceLineRepository invoiceLineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvoiceLineMockMvc;

    private InvoiceLine invoiceLine;

    private InvoiceLine insertedInvoiceLine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLine createEntity() {
        return new InvoiceLine().totalAmount(DEFAULT_TOTAL_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceLine createUpdatedEntity() {
        return new InvoiceLine().totalAmount(UPDATED_TOTAL_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        invoiceLine = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInvoiceLine != null) {
            invoiceLineRepository.delete(insertedInvoiceLine);
            insertedInvoiceLine = null;
        }
    }

    @Test
    @Transactional
    void createInvoiceLine() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvoiceLine
        var returnedInvoiceLine = om.readValue(
            restInvoiceLineMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLine))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvoiceLine.class
        );

        // Validate the InvoiceLine in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvoiceLineUpdatableFieldsEquals(returnedInvoiceLine, getPersistedInvoiceLine(returnedInvoiceLine));

        insertedInvoiceLine = returnedInvoiceLine;
    }

    @Test
    @Transactional
    void createInvoiceLineWithExistingId() throws Exception {
        // Create the InvoiceLine with an existing ID
        invoiceLine.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        invoiceLine.setTotalAmount(null);

        // Create the InvoiceLine, which fails.

        restInvoiceLineMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLine)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvoiceLines() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get all the invoiceLineList
        restInvoiceLineMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));
    }

    @Test
    @Transactional
    void getInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        // Get the invoiceLine
        restInvoiceLineMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceLine.getId().intValue()))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getNonExistingInvoiceLine() throws Exception {
        // Get the invoiceLine
        restInvoiceLineMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine
        InvoiceLine updatedInvoiceLine = invoiceLineRepository.findById(invoiceLine.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvoiceLine are not directly saved in db
        em.detach(updatedInvoiceLine);
        updatedInvoiceLine.totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvoiceLine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvoiceLine))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvoiceLineToMatchAllProperties(updatedInvoiceLine);
    }

    @Test
    @Transactional
    void putNonExistingInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceLine.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(invoiceLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(invoiceLine)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvoiceLineWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine using partial update
        InvoiceLine partialUpdatedInvoiceLine = new InvoiceLine();
        partialUpdatedInvoiceLine.setId(invoiceLine.getId());

        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceLine))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceLineUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvoiceLine, invoiceLine),
            getPersistedInvoiceLine(invoiceLine)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvoiceLineWithPatch() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the invoiceLine using partial update
        InvoiceLine partialUpdatedInvoiceLine = new InvoiceLine();
        partialUpdatedInvoiceLine.setId(invoiceLine.getId());

        partialUpdatedInvoiceLine.totalAmount(UPDATED_TOTAL_AMOUNT);

        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvoiceLine))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceLine in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvoiceLineUpdatableFieldsEquals(partialUpdatedInvoiceLine, getPersistedInvoiceLine(partialUpdatedInvoiceLine));
    }

    @Test
    @Transactional
    void patchNonExistingInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceLine.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(invoiceLine))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvoiceLine() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        invoiceLine.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceLineMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(invoiceLine))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceLine in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvoiceLine() throws Exception {
        // Initialize the database
        insertedInvoiceLine = invoiceLineRepository.saveAndFlush(invoiceLine);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the invoiceLine
        restInvoiceLineMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceLine.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return invoiceLineRepository.count();
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

    protected InvoiceLine getPersistedInvoiceLine(InvoiceLine invoiceLine) {
        return invoiceLineRepository.findById(invoiceLine.getId()).orElseThrow();
    }

    protected void assertPersistedInvoiceLineToMatchAllProperties(InvoiceLine expectedInvoiceLine) {
        assertInvoiceLineAllPropertiesEquals(expectedInvoiceLine, getPersistedInvoiceLine(expectedInvoiceLine));
    }

    protected void assertPersistedInvoiceLineToMatchUpdatableProperties(InvoiceLine expectedInvoiceLine) {
        assertInvoiceLineAllUpdatablePropertiesEquals(expectedInvoiceLine, getPersistedInvoiceLine(expectedInvoiceLine));
    }
}
