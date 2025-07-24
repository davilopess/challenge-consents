package com.davi.challenge.consents.infraestructure.adapter.output.repository.persistence;

import com.davi.challenge.consents.infraestructure.document.ConsentAuditDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsentAuditDocumentRepository extends MongoRepository<ConsentAuditDocument, String> {
}
