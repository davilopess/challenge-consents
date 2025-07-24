package com.davi.challenge.consents.application;

import com.davi.challenge.consents.application.port.input.ConsentAuditService;
import com.davi.challenge.consents.application.port.output.client.AdditionalInfoExternalClient;
import com.davi.challenge.consents.application.port.output.repository.ConsentRepository;
import com.davi.challenge.consents.application.service.ConsentServiceImpl;
import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.request.UpdateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import com.davi.challenge.consents.infraestructure.mapper.ConsentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsentServiceImplTest {

    @Mock
    private ConsentRepository consentRepository;

    @Mock
    private ConsentMapper consentMapper;

    @Mock
    private AdditionalInfoExternalClient additionalInfoExternalClient;

    @Mock
    private ConsentAuditService consentAuditService;

    @InjectMocks
    private ConsentServiceImpl consentService;

    private CreateConsentRequestDTO createConsentRequest;
    private UpdateConsentRequestDTO updateConsentRequest;
    private Consent consent;
    private ConsentResponseDTO consentResponse;
    private UUID consentId;

    @BeforeEach
    void setUp() {
        consentId = UUID.randomUUID();

        createConsentRequest = CreateConsentRequestDTO.builder()
                .cpf("12345678901")
                .expirationDateTime(LocalDateTime.now().plusDays(30))
                .additionalInfo(null)
                .build();

        updateConsentRequest = UpdateConsentRequestDTO.builder()
                .cpf("98765432100")
                .expirationDateTime(LocalDateTime.now().plusDays(60))
                .additionalInfo("Updated additional info")
                .build();

        consent = Consent.builder()
                .id(consentId)
                .cpf("12345678901")
                .expirationDateTime(LocalDateTime.now().plusDays(30))
                .creationDateTime(LocalDateTime.now())
                .status(ConsentStatusEnum.ACTIVE)
                .additionalInfo("Additional info")
                .build();

        consentResponse = ConsentResponseDTO.builder()
                .id(consentId)
                .cpf("12345678901")
                .expirationDateTime(LocalDateTime.now().plusDays(30))
                .creationDateTime(LocalDateTime.now())
                .status(ConsentStatusEnum.ACTIVE)
                .additionalInfo("Additional info")
                .build();
    }

    @Test
    void shouldCreateConsentSuccessfully() {
        Consent mappedConsent = Consent.builder()
                .id(null)
                .cpf("12345678901")
                .expirationDateTime(createConsentRequest.expirationDateTime())
                .additionalInfo(null)
                .creationDateTime(null)
                .status(null)
                .build();

        when(consentMapper.toDomain(createConsentRequest)).thenReturn(mappedConsent);
        when(additionalInfoExternalClient.getAdditionalInfo()).thenReturn("External additional info");
        when(consentRepository.save(any(Consent.class))).thenReturn(consent);
        when(consentMapper.toDTO(consent)).thenReturn(consentResponse);

        ConsentResponseDTO result = consentService.createConsent(createConsentRequest);

        assertNotNull(result);
        assertEquals(consentResponse, result);

        verify(consentMapper).toDomain(createConsentRequest);
        verify(additionalInfoExternalClient).getAdditionalInfo();
        verify(consentRepository).save(any(Consent.class));
        verify(consentMapper).toDTO(consent);
    }

    @Test
    void shouldUpdateConsentSuccessfully() {
        when(consentRepository.findById(consentId)).thenReturn(Optional.of(consent));
        when(consentRepository.save(consent)).thenReturn(consent);
        when(consentMapper.toDTO(consent)).thenReturn(consentResponse);

        ConsentResponseDTO result = consentService.updateConsent(consentId, updateConsentRequest);

        assertNotNull(result);
        assertEquals(consentResponse, result);
        assertEquals(updateConsentRequest.cpf(), consent.getCpf());
        assertEquals(updateConsentRequest.expirationDateTime(), consent.getExpirationDateTime());
        assertEquals(updateConsentRequest.additionalInfo(), consent.getAdditionalInfo());

        verify(consentRepository).findById(consentId);
        verify(consentRepository).save(consent);
        verify(consentAuditService).registerAudit(consent);
        verify(consentMapper).toDTO(consent);
    }

    @Test
    void shouldRevokeConsentSuccessfully() {
        when(consentRepository.findById(consentId)).thenReturn(Optional.of(consent));
        when(consentRepository.save(consent)).thenReturn(consent);

        consentService.revokeConsent(consentId);

        assertEquals(ConsentStatusEnum.REVOKED, consent.getStatus());

        verify(consentRepository).findById(consentId);
        verify(consentRepository).save(consent);
        verify(consentAuditService).registerAudit(consent);
    }
}
