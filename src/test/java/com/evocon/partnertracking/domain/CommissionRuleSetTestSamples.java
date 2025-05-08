package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CommissionRuleSetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CommissionRuleSet getCommissionRuleSetSample1() {
        return new CommissionRuleSet().id(1L).commissionRuleSetId("commissionRuleSetId1").ruleSetName("ruleSetName1");
    }

    public static CommissionRuleSet getCommissionRuleSetSample2() {
        return new CommissionRuleSet().id(2L).commissionRuleSetId("commissionRuleSetId2").ruleSetName("ruleSetName2");
    }

    public static CommissionRuleSet getCommissionRuleSetRandomSampleGenerator() {
        return new CommissionRuleSet()
            .id(longCount.incrementAndGet())
            .commissionRuleSetId(UUID.randomUUID().toString())
            .ruleSetName(UUID.randomUUID().toString());
    }
}
