package com.davi.challenge.consents.infraestructure.adapter.output;

import com.davi.challenge.consents.infraestructure.adapter.output.client.AdditionalInfoExternalClientImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdditionalInfoExternalClientImplIntegrationTest {

    @Autowired
    private AdditionalInfoExternalClientImpl client;

    @Test
    void shouldReturnStringWhenApiWork() {
        String result = client.getAdditionalInfo();

        assertNotNull(result, "A resposta da API não deve ser null");
        assertFalse(result.trim().isEmpty(), "A resposta da API não deve estar vazia");
    }
}
