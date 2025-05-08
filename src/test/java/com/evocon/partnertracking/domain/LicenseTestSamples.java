package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LicenseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static License getLicenseSample1() {
        return new License()
            .id(1L)
            .licenseId("licenseId1")
            .licenseRuleName("licenseRuleName1")
            .licenseQuantity(1)
            .clientId("clientId1")
            .partnerId("partnerId1")
            .commissionRuleSetId("commissionRuleSetId1");
    }

    public static License getLicenseSample2() {
        return new License()
            .id(2L)
            .licenseId("licenseId2")
            .licenseRuleName("licenseRuleName2")
            .licenseQuantity(2)
            .clientId("clientId2")
            .partnerId("partnerId2")
            .commissionRuleSetId("commissionRuleSetId2");
    }

    public static License getLicenseRandomSampleGenerator() {
        return new License()
            .id(longCount.incrementAndGet())
            .licenseId(UUID.randomUUID().toString())
            .licenseRuleName(UUID.randomUUID().toString())
            .licenseQuantity(intCount.incrementAndGet())
            .clientId(UUID.randomUUID().toString())
            .partnerId(UUID.randomUUID().toString())
            .commissionRuleSetId(UUID.randomUUID().toString());
    }
}
