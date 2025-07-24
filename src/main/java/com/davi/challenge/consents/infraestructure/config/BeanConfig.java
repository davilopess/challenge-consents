package com.davi.challenge.consents.infraestructure.config;

import com.davi.challenge.consents.domain.service.ConsentAuditDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ConsentAuditDomainService consentAuditDomainService() {
        return new ConsentAuditDomainService();
    }
}
