package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.ClientTestSamples.*;
import static com.evocon.partnertracking.domain.CommissionFeeTestSamples.*;
import static com.evocon.partnertracking.domain.CommissionRuleSetTestSamples.*;
import static com.evocon.partnertracking.domain.InvoiceLineTestSamples.*;
import static com.evocon.partnertracking.domain.LicenseTestSamples.*;
import static com.evocon.partnertracking.domain.PartnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LicenseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(License.class);
        License license1 = getLicenseSample1();
        License license2 = new License();
        assertThat(license1).isNotEqualTo(license2);

        license2.setId(license1.getId());
        assertThat(license1).isEqualTo(license2);

        license2 = getLicenseSample2();
        assertThat(license1).isNotEqualTo(license2);
    }

    @Test
    void clientTest() {
        License license = getLicenseRandomSampleGenerator();
        Client clientBack = getClientRandomSampleGenerator();

        license.setClient(clientBack);
        assertThat(license.getClient()).isEqualTo(clientBack);

        license.client(null);
        assertThat(license.getClient()).isNull();
    }

    @Test
    void partnerTest() {
        License license = getLicenseRandomSampleGenerator();
        Partner partnerBack = getPartnerRandomSampleGenerator();

        license.setPartner(partnerBack);
        assertThat(license.getPartner()).isEqualTo(partnerBack);

        license.partner(null);
        assertThat(license.getPartner()).isNull();
    }

    @Test
    void commissionRuleSetTest() {
        License license = getLicenseRandomSampleGenerator();
        CommissionRuleSet commissionRuleSetBack = getCommissionRuleSetRandomSampleGenerator();

        license.setCommissionRuleSet(commissionRuleSetBack);
        assertThat(license.getCommissionRuleSet()).isEqualTo(commissionRuleSetBack);

        license.commissionRuleSet(null);
        assertThat(license.getCommissionRuleSet()).isNull();
    }

    @Test
    void invoiceLineTest() {
        License license = getLicenseRandomSampleGenerator();
        InvoiceLine invoiceLineBack = getInvoiceLineRandomSampleGenerator();

        license.setInvoiceLine(invoiceLineBack);
        assertThat(license.getInvoiceLine()).isEqualTo(invoiceLineBack);
        assertThat(invoiceLineBack.getLicense()).isEqualTo(license);

        license.invoiceLine(null);
        assertThat(license.getInvoiceLine()).isNull();
        assertThat(invoiceLineBack.getLicense()).isNull();
    }

    @Test
    void commissionFeeTest() {
        License license = getLicenseRandomSampleGenerator();
        CommissionFee commissionFeeBack = getCommissionFeeRandomSampleGenerator();

        license.setCommissionFee(commissionFeeBack);
        assertThat(license.getCommissionFee()).isEqualTo(commissionFeeBack);
        assertThat(commissionFeeBack.getLicense()).isEqualTo(license);

        license.commissionFee(null);
        assertThat(license.getCommissionFee()).isNull();
        assertThat(commissionFeeBack.getLicense()).isNull();
    }
}
