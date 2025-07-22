package com.davi.challenge.consents.infraestructure.adapter.output.repository.persistence;

import com.davi.challenge.consents.infraestructure.document.ConsentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsentDocumentRepository extends MongoRepository<ConsentDocument, String> {

    Optional<ConsentDocument> findById(UUID id);
}
