package com.davi.challenge.consents.application.port.output;

import com.davi.challenge.consents.domain.entity.Consent;

public interface ConsentRepository {

    Consent save(Consent consent);
}
