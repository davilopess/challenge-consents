package com.davi.challenge.consents.application.port.input;

import com.davi.challenge.consents.domain.entity.Consent;

public interface ConsentAuditService {

    void registerAudit(Consent consent);
}
