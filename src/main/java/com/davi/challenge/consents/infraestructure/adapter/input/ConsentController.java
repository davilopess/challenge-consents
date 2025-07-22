package com.davi.challenge.consents.infraestructure.adapter.input;

import com.davi.challenge.consents.application.port.input.ConsentService;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/consents")
@RequiredArgsConstructor
@Tag(name = "Consent Management", description = "APIs for managing consents")
public class ConsentController {

    private final ConsentService consentService;

    @Operation(summary = "Create a consent", description = "Add a new consent to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consent created successfully",
                    content = @Content(schema = @Schema(implementation = ConsentResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema()))
    })
    @PostMapping
    public ResponseEntity<ConsentResponseDTO> createConsent(@RequestBody @Valid CreateConsentRequestDTO createConsentRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(consentService.createConsent(createConsentRequestDTO));
    }

    @Operation(summary = "Get consent by id", description = "Retrieve consent's details using their id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consent found",
                    content = @Content(schema = @Schema(implementation = ConsentResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Consent not found",
                    content = @Content(schema = @Schema()))
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ConsentResponseDTO> getConsentById(@PathVariable("id") UUID id){
        return ResponseEntity.ok(consentService.getConsentById(id));
    }
}
