package com.evocon.partnertracking.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClientTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Client getClientSample1() {
        Client client = new Client();
        client.setId(1L);
        client.setClientName("clientName1");
        return client;
    }

    public static Client getClientSample2() {
        Client client = new Client();
        client.setId(2L);
        client.setClientName("clientName2");
        return client;
    }

    public static Client getClientRandomSampleGenerator() {
        return new Client()
            .id(longCount.incrementAndGet())
            .clientId(UUID.randomUUID().toString())
            .clientName(UUID.randomUUID().toString())
            .partnerId(UUID.randomUUID().toString());
    }
}
