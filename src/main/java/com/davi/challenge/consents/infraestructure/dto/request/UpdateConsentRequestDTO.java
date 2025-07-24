package com.davi.challenge.consents.infraestructure.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDateTime;

@Builder
public record UpdateConsentRequestDTO(
        @NotBlank
        @CPF
        String cpf,

        LocalDateTime expirationDateTime,

        @Size(min = 1, max = 50, message = "Informações adicionais devem ter entre 1 e 50 caracteres")
        String additionalInfo
) {}
