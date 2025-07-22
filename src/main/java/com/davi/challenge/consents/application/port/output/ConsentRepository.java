package com.davi.challenge.consents.application.port.output;

import com.davi.challenge.consents.domain.entity.Consent;

import java.util.Optional;
import java.util.UUID;

public interface ConsentRepository {

    Consent save(Consent consent);
    Optional<Consent> findById(UUID id);
}
