package com.davi.challenge.consents.infraestructure.adapter.input;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.infraestructure.dto.request.CreateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.request.UpdateConsentRequestDTO;
import com.davi.challenge.consents.infraestructure.dto.response.ConsentResponseDTO;
import com.davi.challenge.consents.infraestructure.exception.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("Consent Controller Integration Tests")
class ConsentControllerIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/consents";
    }

    @AfterAll
    static void afterAll() {
        mongoDBContainer.stop();
    }

    @Nested
    @DisplayName("POST /consents - Create Consent Tests")
    class CreateConsentTests {

        @Test
        @DisplayName("Should create consent successfully with valid data")
        void shouldCreateConsentSuccessfullyWithValidData() {
            CreateConsentRequestDTO request = CreateConsentRequestDTO.builder()
                    .cpf("12345678909")
                    .expirationDateTime(LocalDateTime.now().plusDays(30))
                    .additionalInfo("Test consent creation")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateConsentRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ConsentResponseDTO> response = restTemplate.postForEntity(
                    baseUrl, entity, ConsentResponseDTO.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().id()).isNotNull();
            assertThat(response.getBody().cpf()).isEqualTo("12345678909");
            assertThat(response.getBody().status()).isEqualTo(ConsentStatusEnum.ACTIVE);
            assertThat(response.getBody().additionalInfo()).isEqualTo("Test consent creation");
            assertThat(response.getBody().creationDateTime()).isNotNull();
        }

        @Test
        @DisplayName("Should return 400 for invalid CPF format")
        void shouldReturn400ForInvalidCpfFormat() {
            CreateConsentRequestDTO request = CreateConsentRequestDTO.builder()
                    .cpf("123456789999") // CPF inválido
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateConsentRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                    baseUrl, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody().errors().toString()).contains("cpf");
        }

        @Test
        @DisplayName("Should return 400 for missing required fields")
        void shouldReturn400ForMissingRequiredFields() {
            CreateConsentRequestDTO request = CreateConsentRequestDTO.builder()
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateConsentRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                    baseUrl, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("Should return 400 for additional info exceeding max length")
        void shouldReturn400ForAdditionalInfoExceedingMaxLength() {
            String longAdditionalInfo = "A".repeat(51);
            CreateConsentRequestDTO request = CreateConsentRequestDTO.builder()
                    .cpf("12345678909")
                    .additionalInfo(longAdditionalInfo)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<CreateConsentRequestDTO> entity = new HttpEntity<>(request, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                    baseUrl, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("GET /consents/{id} - Get Consent By ID Tests")
    class GetConsentByIdTests {

        @Test
        @DisplayName("Should get consent by ID successfully")
        void shouldGetConsentByIdSuccessfully() {
            UUID consentId = createTestConsent("61757152091");

            System.out.println(consentId);
            ResponseEntity<ConsentResponseDTO> response = restTemplate.getForEntity(
                    baseUrl + "/" + consentId, ConsentResponseDTO.class);

            System.out.println(response);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().id()).isEqualTo(consentId);
            assertThat(response.getBody().cpf()).isEqualTo("61757152091");
        }

        @Test
        @DisplayName("Should return 404 for non-existent consent")
        void shouldReturn404ForNonExistentConsent() {
            UUID nonExistentId = UUID.randomUUID();

            ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                    baseUrl + "/" + nonExistentId, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("GET /consents - Get All Consents Tests")
    class GetAllConsentsTests {

        @Test
        @DisplayName("Should get all consents with default pagination")
        void shouldGetAllConsentsWithDefaultPagination() {
            createTestConsent("16636697083");
            createTestConsent("37308835006");
            createTestConsent("29756640014");

            ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                assertThat(jsonNode.has("content")).isTrue();
                assertThat(jsonNode.has("pageable")).isTrue();
                assertThat(jsonNode.has("totalElements")).isTrue();
                assertThat(jsonNode.get("content").isArray()).isTrue();
            } catch (JsonProcessingException e) {
                fail("Failed to parse JSON response", e);
            }
        }
    }

    @Nested
    @DisplayName("PUT /consents/{id} - Update Consent Tests")
    class UpdateConsentTests {

        @Test
        @DisplayName("Should update consent successfully")
        void shouldUpdateConsentSuccessfully() {
            UUID consentId = createTestConsent("57144193082");

            UpdateConsentRequestDTO updateRequest = UpdateConsentRequestDTO.builder()
                    .cpf("57144193082")
                    .expirationDateTime(LocalDateTime.now().plusDays(60))
                    .additionalInfo("Updated additional info")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateConsentRequestDTO> entity = new HttpEntity<>(updateRequest, headers);

            ResponseEntity<ConsentResponseDTO> response = restTemplate.exchange(
                    baseUrl + "/" + consentId, HttpMethod.PUT, entity, ConsentResponseDTO.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().id()).isEqualTo(consentId);
            assertThat(response.getBody().additionalInfo()).isEqualTo("Updated additional info");
        }

        @Test
        @DisplayName("Should return 404 when updating non-existent consent")
        void shouldReturn404WhenUpdatingNonExistentConsent() {
            UUID nonExistentId = UUID.randomUUID();

            UpdateConsentRequestDTO updateRequest = UpdateConsentRequestDTO.builder()
                    .cpf("77858165062")
                    .additionalInfo("Updated info")
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateConsentRequestDTO> entity = new HttpEntity<>(updateRequest, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    baseUrl + "/" + nonExistentId, HttpMethod.PUT, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return 400 when updating with invalid data")
        void shouldReturn400WhenUpdatingWithInvalidData() {
            UUID consentId = createTestConsent("43278152093");

            UpdateConsentRequestDTO updateRequest = UpdateConsentRequestDTO.builder()
                    .cpf("77858165062")
                    .additionalInfo("A".repeat(51))
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UpdateConsentRequestDTO> entity = new HttpEntity<>(updateRequest, headers);

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    baseUrl + "/" + consentId, HttpMethod.PUT, entity, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("DELETE /consents/{id} - Revoke Consent Tests")
    class RevokeConsentTests {

        @Test
        @DisplayName("Should revoke consent successfully")
        void shouldRevokeConsentSuccessfully() {
            UUID consentId = createTestConsent("57311804078");

            ResponseEntity<String> response = restTemplate.exchange(
                    baseUrl + "/" + consentId, HttpMethod.DELETE, null, String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("Consent revoked successfully");

            ResponseEntity<ConsentResponseDTO> getResponse = restTemplate.getForEntity(
                    baseUrl + "/" + consentId, ConsentResponseDTO.class);

            assertThat(getResponse.getBody().status()).isEqualTo(ConsentStatusEnum.REVOKED);
        }

        @Test
        @DisplayName("Should return 404 when revoking non-existent consent")
        void shouldReturn404WhenRevokingNonExistentConsent() {
            UUID nonExistentId = UUID.randomUUID();

            ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                    baseUrl + "/" + nonExistentId, HttpMethod.DELETE, null, ErrorResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    private UUID createTestConsent(String cpf) {
        CreateConsentRequestDTO request = CreateConsentRequestDTO.builder()
                .cpf(cpf)
                .expirationDateTime(LocalDateTime.now().plusDays(30))
                .additionalInfo("Test consent")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateConsentRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<ConsentResponseDTO> response = restTemplate.postForEntity(
                baseUrl, entity, ConsentResponseDTO.class);

        return response.getBody().id();
    }

}
