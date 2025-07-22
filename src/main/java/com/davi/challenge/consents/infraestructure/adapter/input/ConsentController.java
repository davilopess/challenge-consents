package com.davi.challenge.consents.infraestructure.adapter.input;

import com.davi.challenge.consents.application.port.input.ConsentService;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consents")
@RequiredArgsConstructor
public class ConsentController {

    private final ConsentService consentService;

    @PostMapping
    public ResponseEntity<ConsentResponseDTO> createConsent(@RequestBody @Valid CreateConsentRequestDTO createConsentRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(consentService.createConsent(createConsentRequestDTO));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsentResponseDTO> getConsentById(@PathVariable("id") UUID id){
        return ResponseEntity.ok(consentService.getConsentById(id));
    }
}
