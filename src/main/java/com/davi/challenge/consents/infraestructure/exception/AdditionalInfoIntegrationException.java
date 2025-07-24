package com.davi.challenge.consents.infraestructure.exception;

public class AdditionalInfoIntegrationException extends RuntimeException{

    public AdditionalInfoIntegrationException(){
        super("Failed to fetch additional information.");
    }
}
