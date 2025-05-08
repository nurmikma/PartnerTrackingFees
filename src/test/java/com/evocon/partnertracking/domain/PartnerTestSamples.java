package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PartnerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Partner getPartnerSample1() {
        return new Partner().id(1L).partnerID("partnerID1").partnerName("partnerName1");
    }

    public static Partner getPartnerSample2() {
        return new Partner().id(2L).partnerID("partnerID2").partnerName("partnerName2");
    }

    public static Partner getPartnerRandomSampleGenerator() {
        return new Partner()
            .id(longCount.incrementAndGet())
            .partnerID(UUID.randomUUID().toString())
            .partnerName(UUID.randomUUID().toString());
    }
}
