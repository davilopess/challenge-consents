package com.davi.challenge.consents.domain.entity;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class Consent {

    private UUID id;
    private String cpf;
    private ConsentStatusEnum status;
    private LocalDateTime creationDateTime;
    private LocalDateTime expirationDateTime;
    private String additionalInfo;

    public void defineStatus(){
        if(expirationDateTime != null && expirationDateTime.isBefore(LocalDateTime.now())){
            this.status = ConsentStatusEnum.EXPIRED;
            return;
        }

        this.status = ConsentStatusEnum.ACTIVE;
    }

    public boolean isRevoked(){
        return status != null
                && status.equals(ConsentStatusEnum.REVOKED);
    }

    public void setAdditionalInfo(String additionalInfo) {
        if (additionalInfo != null && (additionalInfo.isEmpty() || additionalInfo.length() > 50)) {
            throw new IllegalArgumentException("Additional info must have between 1 and 50 characters when present");
        }
        this.additionalInfo = additionalInfo;
    }
}
