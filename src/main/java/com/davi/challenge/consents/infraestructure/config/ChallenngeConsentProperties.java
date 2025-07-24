package com.davi.challenge.consents.infraestructure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ChallenngeConsentProperties {

    @Value("${additional.info.integrator.url}")
    private String additionalInfoIntegratorUrl;
}
