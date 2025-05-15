package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.CommissionRuleSetTestSamples.*;
import static com.evocon.partnertracking.domain.CommissionRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommissionRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommissionRule.class);
        CommissionRule commissionRule1 = getCommissionRuleSample1();
        CommissionRule commissionRule2 = new CommissionRule();
        assertThat(commissionRule1).isNotEqualTo(commissionRule2);

        commissionRule2.setId(commissionRule1.getId());
        assertThat(commissionRule1).isEqualTo(commissionRule2);

        commissionRule2 = getCommissionRuleSample2();
        assertThat(commissionRule1).isNotEqualTo(commissionRule2);
    }

    @Test
    void commissionRuleSetTest() {
        CommissionRule commissionRule = getCommissionRuleRandomSampleGenerator();
        CommissionRuleSet commissionRuleSetBack = getCommissionRuleSetRandomSampleGenerator();

        commissionRule.setCommissionRuleSet(commissionRuleSetBack);
        assertThat(commissionRule.getCommissionRuleSet()).isEqualTo(commissionRuleSetBack);

        commissionRule.commissionRuleSet(null);
        assertThat(commissionRule.getCommissionRuleSet()).isNull();
    }
}
