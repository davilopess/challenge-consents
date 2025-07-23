package com.davi.challenge.consents.infraestructure.adapter.output.client;

import com.davi.challenge.consents.application.port.output.client.AdditionalInfoExternalClient;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AdditionalInfoExternalClientImpl implements AdditionalInfoExternalClient {

    @Override
    public String getAdditionalInfo() {
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject("https://api.github.com/users/martinfowler", String.class);
    }
}
