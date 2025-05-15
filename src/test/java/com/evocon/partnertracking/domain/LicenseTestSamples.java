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
        return new License().id(1L).licenseRuleName("licenseRuleName1").licenseQuantity(1);
    }

    public static License getLicenseSample2() {
        return new License().id(2L).licenseRuleName("licenseRuleName2").licenseQuantity(2);
    }

    public static License getLicenseRandomSampleGenerator() {
        return new License()
            .id(longCount.incrementAndGet())
            .licenseRuleName(UUID.randomUUID().toString())
            .licenseQuantity(intCount.incrementAndGet());
    }
}
