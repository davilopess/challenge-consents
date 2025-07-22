package com.davi.challenge.consents.application.port.input;

import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;

import java.util.UUID;

public interface ConsentService {

    ConsentResponseDTO createConsent(CreateConsentRequestDTO createConsentRequestDTO);

    ConsentResponseDTO getConsentById(UUID id);
}
