package com.davi.challenge.consents.infraestructure.document;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "consents")
public record ConsentDocument(
        @Id UUID id,
        String cpf,
        ConsentStatusEnum status,
        LocalDateTime creationDateTime,
        LocalDateTime expirationDateTime,
        String additionalInfo
) {}