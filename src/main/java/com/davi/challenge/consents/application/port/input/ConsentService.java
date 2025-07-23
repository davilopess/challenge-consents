package com.davi.challenge.consents.application.port.input;

import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.request.UpdateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ConsentService {

    ConsentResponseDTO createConsent(CreateConsentRequestDTO createConsentRequestDTO);

    ConsentResponseDTO updateConsent(UUID id, UpdateConsentRequestDTO updateConsentRequestDTO);

    ConsentResponseDTO getConsentById(UUID id);

    Page<ConsentResponseDTO> getAllConsents(Pageable pageable);

    void revokeConsent(UUID id);
}
