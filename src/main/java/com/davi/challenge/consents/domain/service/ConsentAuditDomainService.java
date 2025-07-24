package com.davi.challenge.consents.domain.service;

import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.entity.ConsentAudit;
import com.davi.challenge.consents.domain.enums.OperationTypeEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConsentAuditDomainService {

    public ConsentAudit createConsentAudit(Consent consent){
        ConsentAudit consentAudit = new ConsentAudit();
        consentAudit.setId(UUID.randomUUID());
        consentAudit.setConsentId(consent.getId());
        consentAudit.setCpf(consent.getCpf());
        consentAudit.setStatus(consent.getStatus());
        consentAudit.setCreationDateTime(consent.getCreationDateTime());
        consentAudit.setExpirationDateTime(consent.getExpirationDateTime());
        consentAudit.setAdditionalInfo(consent.getAdditionalInfo());
        consentAudit.setChangedAt(LocalDateTime.now());

        OperationTypeEnum operationTypeEnum = consent.isRevoked()
                ? OperationTypeEnum.DELETE
                : OperationTypeEnum.PUT;

        consentAudit.setOperationType(operationTypeEnum);

        return consentAudit;
    }
}
