package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CommissionRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CommissionRule getCommissionRuleSample1() {
        return new CommissionRule().id(1L).ruleName("ruleName1").description("description1").startDay(1).endDay(1);
    }

    public static CommissionRule getCommissionRuleSample2() {
        return new CommissionRule().id(2L).ruleName("ruleName2").description("description2").startDay(2).endDay(2);
    }

    public static CommissionRule getCommissionRuleRandomSampleGenerator() {
        return new CommissionRule()
            .id(longCount.incrementAndGet())
            .ruleName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .startDay(intCount.incrementAndGet())
            .endDay(intCount.incrementAndGet());
    }
}
