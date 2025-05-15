package com.evocon.partnertracking.domain;

import static com.evocon.partnertracking.domain.ClientTestSamples.*;
import static com.evocon.partnertracking.domain.PartnerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.evocon.partnertracking.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = getClientSample1();
        Client client2 = new Client();
        assertThat(client1).isNotEqualTo(client2);

        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);

        client2 = getClientSample2();
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    void partnerTest() {
        Client client = getClientRandomSampleGenerator();
        Partner partnerBack = getPartnerRandomSampleGenerator();

        client.setPartner(partnerBack);
        assertThat(client.getPartner()).isEqualTo(partnerBack);

        client.setPartner(null);
        assertThat(client.getPartner()).isNull();
    }
}
