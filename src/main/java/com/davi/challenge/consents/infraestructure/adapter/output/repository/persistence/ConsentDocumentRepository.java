package com.davi.challenge.consents.infraestructure.adapter.output.repository.persistence;

import com.davi.challenge.consents.infraestructure.document.ConsentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConsentDocumentRepository extends MongoRepository<ConsentDocument, String> {
}
