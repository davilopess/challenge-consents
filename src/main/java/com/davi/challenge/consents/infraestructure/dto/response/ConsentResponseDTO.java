package com.davi.challenge.consents.infraestructure.dto.response;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ConsentResponseDTO(
        UUID id,
        String cpf,
        ConsentStatusEnum status,
        LocalDateTime creationDateTime,
        LocalDateTime expirationDateTime,
        String additionalInfo
) {}
