package com.davi.challenge.consents.infraestructure.adapter.output.repository;

import com.davi.challenge.consents.application.port.output.repository.ConsentAuditRepository;
import com.davi.challenge.consents.domain.entity.ConsentAudit;
import com.davi.challenge.consents.infraestructure.adapter.output.repository.persistence.ConsentAuditDocumentRepository;
import com.davi.challenge.consents.infraestructure.mapper.ConsentAuditMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ConsentAuditRepositoryImpl implements ConsentAuditRepository {

    private final ConsentAuditDocumentRepository repository;
    private final ConsentAuditMapper consentAuditMapper;

    @Override
    public void save(ConsentAudit consentAudit) {
        repository.save(consentAuditMapper.toDocument(consentAudit));
    }
}
