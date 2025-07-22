package com.davi.challenge.consents.infraestructure.mapper;

import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.infraestructure.document.ConsentDocument;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsentMapper {

    Consent toDomain(CreateConsentRequestDTO createConsentRequestDTO);
    Consent toDomain(ConsentDocument consentDocument);
    ConsentDocument toDocument(Consent consent);
    ConsentResponseDTO toDTO(Consent consent);
}
