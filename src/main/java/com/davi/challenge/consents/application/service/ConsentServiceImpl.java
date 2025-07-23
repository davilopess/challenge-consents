package com.davi.challenge.consents.application.service;

import com.davi.challenge.consents.application.port.input.ConsentService;
import com.davi.challenge.consents.application.port.output.ConsentRepository;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.request.UpdateConsentRequestDTO;
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
    public ConsentResponseDTO updateConsent(UUID id, UpdateConsentRequestDTO updateConsentRequestDTO) {
        Consent existingConsent = consentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        existingConsent.setCpf(updateConsentRequestDTO.cpf());
        existingConsent.setExpirationDateTime(updateConsentRequestDTO.expirationDateTime());
        existingConsent.setAdditionalInfo(updateConsentRequestDTO.additionalInfo());
        existingConsent.defineStatus();
        return consentMapper.toDTO(consentRepository.save(existingConsent));
    }

    @Override
    public ConsentResponseDTO getConsentById(UUID id) {
        Consent consent = consentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        return consentMapper.toDTO(consent);
    }

    @Override
    public void revokeConsent(UUID id) {
        Consent consent = consentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        consent.setStatus(ConsentStatusEnum.REVOKED);
        consentRepository.save(consent);
    }

}
