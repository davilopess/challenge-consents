package com.davi.challenge.consents.infraestructure.adapter.output.client;

import com.davi.challenge.consents.application.port.output.client.AdditionalInfoExternalClient;
import com.davi.challenge.consents.infraestructure.config.ChallenngeConsentProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class AdditionalInfoExternalClientImpl implements AdditionalInfoExternalClient {

    private final ChallenngeConsentProperties properties;

    @Override
    public String getAdditionalInfo() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(
                properties.getAdditionalInfoIntegratorUrl(),
                String.class
        );
    }
}
