package com.davi.challenge.consents.application.service;

import com.davi.challenge.consents.application.port.input.ConsentAuditService;
import com.davi.challenge.consents.application.port.input.ConsentService;
import com.davi.challenge.consents.application.port.output.client.AdditionalInfoExternalClient;
import com.davi.challenge.consents.application.port.output.repository.ConsentRepository;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.request.UpdateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import com.davi.challenge.consents.infraestructure.exception.ConsentNotFoundException;
import com.davi.challenge.consents.infraestructure.mapper.ConsentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ConsentServiceImpl implements ConsentService {
    private final ConsentRepository consentRepository;
    private final ConsentMapper consentMapper;
    private final AdditionalInfoExternalClient additionalInfoExternalClient;
    private final ConsentAuditService consentAuditService;

    @Override
    public ConsentResponseDTO createConsent(CreateConsentRequestDTO createConsentRequestDTO) {
        Consent consent = consentMapper.toDomain(createConsentRequestDTO);
        consent.setId(UUID.randomUUID());
        consent.defineStatus();
        consent.setCreationDateTime(LocalDateTime.now());
        if(Objects.isNull(consent.getAdditionalInfo())) consent.setAdditionalInfo(additionalInfoExternalClient.getAdditionalInfo());
        return consentMapper.toDTO(consentRepository.save(consent));
    }

    @Override
    public ConsentResponseDTO updateConsent(UUID id, UpdateConsentRequestDTO updateConsentRequestDTO) {
        Consent existingConsent = consentRepository.findById(id).orElseThrow(ConsentNotFoundException::new);
        existingConsent.setCpf(updateConsentRequestDTO.cpf());
        existingConsent.setExpirationDateTime(updateConsentRequestDTO.expirationDateTime());
        existingConsent.setAdditionalInfo(updateConsentRequestDTO.additionalInfo());
        existingConsent.defineStatus();

        Consent existingConsentUpdated = consentRepository.save(existingConsent);
        consentAuditService.registerAudit(existingConsentUpdated);

        return consentMapper.toDTO(existingConsentUpdated);
    }

    @Override
    public ConsentResponseDTO getConsentById(UUID id) {
        Consent consent = consentRepository.findById(id).orElseThrow(ConsentNotFoundException::new);
        return consentMapper.toDTO(consent);
    }

    @Override
    public Page<ConsentResponseDTO> getAllConsents(Pageable pageable) {
        return consentRepository.findAll(pageable).map(consentMapper::toDTO);
    }

    @Override
    public void revokeConsent(UUID id) {
        Consent consent = consentRepository.findById(id).orElseThrow(ConsentNotFoundException::new);
        consent.setStatus(ConsentStatusEnum.REVOKED);
        consentRepository.save(consent);

        consentAuditService.registerAudit(consent);
    }

}
