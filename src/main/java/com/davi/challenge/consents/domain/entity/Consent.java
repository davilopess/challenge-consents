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
}
