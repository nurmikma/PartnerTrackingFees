package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class CommissionFeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CommissionFee getCommissionFeeSample1() {
        return new CommissionFee().id(1L);
    }

    public static CommissionFee getCommissionFeeSample2() {
        return new CommissionFee().id(2L);
    }

    public static CommissionFee getCommissionFeeRandomSampleGenerator() {
        return new CommissionFee().id(longCount.incrementAndGet());
    }
}
