package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.CommissionRuleSetTestSamples.*;
import static com.evocon.partnertracking.domain.CommissionRuleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CommissionRuleSetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CommissionRuleSet.class);
        CommissionRuleSet commissionRuleSet1 = getCommissionRuleSetSample1();
        CommissionRuleSet commissionRuleSet2 = new CommissionRuleSet();
        assertThat(commissionRuleSet1).isNotEqualTo(commissionRuleSet2);

        commissionRuleSet2.setId(commissionRuleSet1.getId());
        assertThat(commissionRuleSet1).isEqualTo(commissionRuleSet2);

        commissionRuleSet2 = getCommissionRuleSetSample2();
        assertThat(commissionRuleSet1).isNotEqualTo(commissionRuleSet2);
    }

    @Test
    void rulesTest() {
        CommissionRuleSet commissionRuleSet = getCommissionRuleSetRandomSampleGenerator();
        CommissionRule commissionRuleBack = getCommissionRuleRandomSampleGenerator();

        commissionRuleSet.addRules(commissionRuleBack);
        assertThat(commissionRuleSet.getRules()).containsOnly(commissionRuleBack);
        assertThat(commissionRuleBack.getCommissionRuleSet()).isEqualTo(commissionRuleSet);

        commissionRuleSet.removeRules(commissionRuleBack);
        assertThat(commissionRuleSet.getRules()).doesNotContain(commissionRuleBack);
        assertThat(commissionRuleBack.getCommissionRuleSet()).isNull();

        commissionRuleSet.rules(new HashSet<>(Set.of(commissionRuleBack)));
        assertThat(commissionRuleSet.getRules()).containsOnly(commissionRuleBack);
        assertThat(commissionRuleBack.getCommissionRuleSet()).isEqualTo(commissionRuleSet);

        commissionRuleSet.setRules(new HashSet<>());
        assertThat(commissionRuleSet.getRules()).doesNotContain(commissionRuleBack);
        assertThat(commissionRuleBack.getCommissionRuleSet()).isNull();
    }
}
