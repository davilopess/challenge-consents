package com.davi.challenge.consents.application.port.output.repository;

import com.davi.challenge.consents.domain.entity.ConsentAudit;

public interface ConsentAuditRepository {

    void save(ConsentAudit consentAudit);
}
