package com.davi.challenge.consents.infraestructure.document;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.domain.enums.OperationTypeEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "consents_audit")
public record ConsentAuditDocument(
    @Id UUID id,
    UUID consentId,
    String cpf,
    ConsentStatusEnum status,
    LocalDateTime creationDateTime,
    LocalDateTime expirationDateTime,
    String additionalInfo,
    OperationTypeEnum operationType,
    LocalDateTime changedAt
){}
