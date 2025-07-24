package com.davi.challenge.consents.infraestructure.mapper;

import com.davi.challenge.consents.domain.entity.ConsentAudit;
import com.davi.challenge.consents.infraestructure.document.ConsentAuditDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsentAuditMapper {

    ConsentAuditDocument toDocument(ConsentAudit consentAudit);
}
