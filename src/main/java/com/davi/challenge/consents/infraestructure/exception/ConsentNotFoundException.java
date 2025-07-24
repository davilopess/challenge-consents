package com.davi.challenge.consents.infraestructure.exception;

public class ConsentNotFoundException extends RuntimeException{

    public ConsentNotFoundException(){
        super("Consent not found.");
    }
}
