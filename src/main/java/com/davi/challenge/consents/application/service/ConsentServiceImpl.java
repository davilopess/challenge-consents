package com.davi.challenge.consents.application.service;

import com.davi.challenge.consents.application.port.input.ConsentService;
import com.davi.challenge.consents.application.port.output.ConsentRepository;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import com.davi.challenge.consents.infraestructure.mapper.ConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ConsentServiceImpl implements ConsentService {
    private final ConsentRepository consentRepository;
    private final ConsentMapper consentMapper;

    @Override
    public ConsentResponseDTO createConsent(CreateConsentRequestDTO createConsentRequestDTO) {
        Consent consent = consentMapper.toDomain(createConsentRequestDTO);
        consent.setId(UUID.randomUUID());
        consent.defineStatus();
        consent.setCreationDateTime(LocalDateTime.now());
        return consentMapper.toDTO(consentRepository.save(consent));
    }

    @Override
    public ConsentResponseDTO getConsentById(UUID id) {
        Consent consent = consentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        return consentMapper.toDTO(consent);
    }
}
