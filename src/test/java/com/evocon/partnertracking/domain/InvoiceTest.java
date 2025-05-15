package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.ClientTestSamples.*;
import static com.evocon.partnertracking.domain.InvoiceLineTestSamples.*;
import static com.evocon.partnertracking.domain.InvoiceTestSamples.*;
import static com.evocon.partnertracking.domain.PartnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Invoice.class);
        Invoice invoice1 = getInvoiceSample1();
        Invoice invoice2 = new Invoice();
        assertThat(invoice1).isNotEqualTo(invoice2);

        invoice2.setId(invoice1.getId());
        assertThat(invoice1).isEqualTo(invoice2);

        invoice2 = getInvoiceSample2();
        assertThat(invoice1).isNotEqualTo(invoice2);
    }

    @Test
    void lineItemsTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        InvoiceLine invoiceLineBack = getInvoiceLineRandomSampleGenerator();

        invoice.addLineItems(invoiceLineBack);
        assertThat(invoice.getLineItems()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.removeLineItems(invoiceLineBack);
        assertThat(invoice.getLineItems()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();

        invoice.lineItems(new HashSet<>(Set.of(invoiceLineBack)));
        assertThat(invoice.getLineItems()).containsOnly(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isEqualTo(invoice);

        invoice.setLineItems(new HashSet<>());
        assertThat(invoice.getLineItems()).doesNotContain(invoiceLineBack);
        assertThat(invoiceLineBack.getInvoice()).isNull();
    }

    @Test
    void clientTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        invoice.setClient(clientBack);
        assertThat(invoice.getClient()).isEqualTo(clientBack);

        invoice.client(null);
        assertThat(invoice.getClient()).isNull();
    }

    @Test
    void partnerTest() {
        Invoice invoice = getInvoiceRandomSampleGenerator();
        Partner partnerBack = getPartnerRandomSampleGenerator();

        invoice.setPartner(partnerBack);
        assertThat(invoice.getPartner()).isEqualTo(partnerBack);

        invoice.partner(null);
        assertThat(invoice.getPartner()).isNull();
    }
}
