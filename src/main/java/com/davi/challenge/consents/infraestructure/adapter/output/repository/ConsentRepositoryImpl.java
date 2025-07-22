package com.davi.challenge.consents.infraestructure.adapter.output.repository;

import com.davi.challenge.consents.application.port.output.ConsentRepository;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.infraestructure.adapter.output.repository.persistence.ConsentDocumentRepository;
import com.davi.challenge.consents.infraestructure.document.ConsentDocument;
import com.davi.challenge.consents.infraestructure.mapper.ConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConsentRepositoryImpl implements ConsentRepository {
    private final ConsentDocumentRepository repository;
    private final ConsentMapper consentMapper;

    @Override
    public Consent save(Consent consent) {
        ConsentDocument consentDocument = consentMapper.toDocument(consent);
        return consentMapper.toDomain(repository.save(consentDocument));
    }

    @Override
    public Optional<Consent> findById(UUID id) {
        return repository.findById(id).map(consentMapper::toDomain);
    }
}
