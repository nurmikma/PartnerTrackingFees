package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.CommissionFeeTestSamples.*;
import static com.evocon.partnertracking.domain.LicenseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommissionFeeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommissionFee.class);
        CommissionFee commissionFee1 = getCommissionFeeSample1();
        CommissionFee commissionFee2 = new CommissionFee();
        assertThat(commissionFee1).isNotEqualTo(commissionFee2);

        commissionFee2.setId(commissionFee1.getId());
        assertThat(commissionFee1).isEqualTo(commissionFee2);

        commissionFee2 = getCommissionFeeSample2();
        assertThat(commissionFee1).isNotEqualTo(commissionFee2);
    }

    @Test
    void licenseTest() {
        CommissionFee commissionFee = getCommissionFeeRandomSampleGenerator();
        License licenseBack = getLicenseRandomSampleGenerator();

        commissionFee.setLicense(licenseBack);
        assertThat(commissionFee.getLicense()).isEqualTo(licenseBack);

        commissionFee.license(null);
        assertThat(commissionFee.getLicense()).isNull();
    }
}
