package com.davi.challenge.consents.domain.entity;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.domain.enums.OperationTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsentAudit {

    private UUID id;
    private UUID consentId;
    private String cpf;
    private ConsentStatusEnum status;
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;
    private String additionalInfo;
    private OperationTypeEnum operationType;
    private LocalDateTime changedAt;
}
