package com.davi.challenge.consents.application.service;

import com.davi.challenge.consents.application.port.input.ConsentAuditService;
import com.davi.challenge.consents.application.port.output.repository.ConsentAuditRepository;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.entity.ConsentAudit;
import com.davi.challenge.consents.domain.service.ConsentAuditDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsentAuditServiceImpl implements ConsentAuditService {
    private final ConsentAuditRepository consentAuditRepository;
    private final ConsentAuditDomainService consentAuditDomainService;

    @Override
    public void registerAudit(Consent consent) {
        ConsentAudit consentAudit = consentAuditDomainService.createConsentAudit(consent);
        consentAuditRepository.save(consentAudit);
    }
}
