package com.davi.challenge.consents.domain.service;

import com.davi.challenge.consents.domain.entity.Consent;
import com.davi.challenge.consents.domain.entity.ConsentAudit;
import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.domain.enums.OperationTypeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ConsentAuditDomainService Tests")
class ConsentAuditDomainServiceTest {

    private ConsentAuditDomainService consentAuditDomainService;

    @BeforeEach
    void setUp() {
        consentAuditDomainService = new ConsentAuditDomainService();
    }

    @Nested
    @DisplayName("createConsentAudit() Method Tests")
    class CreateConsentAuditTests {

        @Test
        @DisplayName("Should create audit with PUT operation for ACTIVE consent")
        void shouldCreateAuditWithPutOperationForActiveConsent() {
            UUID consentId = UUID.randomUUID();
            LocalDateTime creationDateTime = LocalDateTime.now().minusDays(1);
            LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(30);

            Consent consent = new Consent();
            consent.setId(consentId);
            consent.setCpf("12345678900");
            consent.setStatus(ConsentStatusEnum.ACTIVE);
            consent.setCreationDateTime(creationDateTime);
            consent.setExpirationDateTime(expirationDateTime);
            consent.setAdditionalInfo("Test additional info");

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit).isNotNull();
            assertThat(audit.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
        }

        @Test
        @DisplayName("Should create audit with DELETE operation for REVOKED consent")
        void shouldCreateAuditWithDeleteOperationForRevokedConsent() {
            UUID consentId = UUID.randomUUID();
            LocalDateTime creationDateTime = LocalDateTime.now().minusDays(1);

            Consent consent = new Consent();
            consent.setId(consentId);
            consent.setCpf("98765432100");
            consent.setStatus(ConsentStatusEnum.REVOKED);
            consent.setCreationDateTime(creationDateTime);
            consent.setAdditionalInfo("Revoked consent info");

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit).isNotNull();
            assertThat(audit.getStatus()).isEqualTo(ConsentStatusEnum.REVOKED);
            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.DELETE);
        }

        @Test
        @DisplayName("Should create audit with PUT operation for EXPIRED consent")
        void shouldCreateAuditWithPutOperationForExpiredConsent() {
            UUID consentId = UUID.randomUUID();
            LocalDateTime creationDateTime = LocalDateTime.now().minusDays(2);
            LocalDateTime expirationDateTime = LocalDateTime.now().minusDays(1);

            Consent consent = new Consent();
            consent.setId(consentId);
            consent.setCpf("11122233344");
            consent.setStatus(ConsentStatusEnum.EXPIRED);
            consent.setCreationDateTime(creationDateTime);
            consent.setExpirationDateTime(expirationDateTime);

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit).isNotNull();
            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
            assertThat(audit.getStatus()).isEqualTo(ConsentStatusEnum.EXPIRED);
        }

        @Test
        @DisplayName("Should copy all consent properties correctly")
        void shouldCopyAllConsentPropertiesCorrectly() {
            UUID consentId = UUID.randomUUID();
            String cpf = "55566677788";
            ConsentStatusEnum status = ConsentStatusEnum.ACTIVE;
            LocalDateTime creationDateTime = LocalDateTime.of(2023, 1, 15, 10, 30, 45);
            LocalDateTime expirationDateTime = LocalDateTime.of(2024, 1, 15, 10, 30, 45);
            String additionalInfo = "Specific additional information";

            Consent consent = new Consent();
            consent.setId(consentId);
            consent.setCpf(cpf);
            consent.setStatus(status);
            consent.setCreationDateTime(creationDateTime);
            consent.setExpirationDateTime(expirationDateTime);
            consent.setAdditionalInfo(additionalInfo);

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit.getConsentId()).isEqualTo(consentId);
            assertThat(audit.getCpf()).isEqualTo(cpf);
            assertThat(audit.getStatus()).isEqualTo(status);
            assertThat(audit.getCreationDateTime()).isEqualTo(creationDateTime);
            assertThat(audit.getExpirationDateTime()).isEqualTo(expirationDateTime);
            assertThat(audit.getAdditionalInfo()).isEqualTo(additionalInfo);
        }

        @Test
        @DisplayName("Should handle consent with null expiration date")
        void shouldHandleConsentWithNullExpirationDate() {
            Consent consent = new Consent();
            consent.setId(UUID.randomUUID());
            consent.setCpf("12345678900");
            consent.setStatus(ConsentStatusEnum.ACTIVE);
            consent.setCreationDateTime(LocalDateTime.now());
            consent.setExpirationDateTime(null);
            consent.setAdditionalInfo("No expiration");

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit.getExpirationDateTime()).isNull();
            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
        }

        @Test
        @DisplayName("Should handle consent with null additional info")
        void shouldHandleConsentWithNullAdditionalInfo() {
            Consent consent = new Consent();
            consent.setId(UUID.randomUUID());
            consent.setCpf("12345678900");
            consent.setStatus(ConsentStatusEnum.ACTIVE);
            consent.setCreationDateTime(LocalDateTime.now());
            consent.setAdditionalInfo(null);

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit.getAdditionalInfo()).isNull();
            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
        }

        @Test
        @DisplayName("Should set changedAt to current time")
        void shouldSetChangedAtToCurrentTime() {
            LocalDateTime beforeExecution = LocalDateTime.now();
            Consent consent = createTestConsent(ConsentStatusEnum.ACTIVE);

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);
            LocalDateTime afterExecution = LocalDateTime.now();

            assertThat(audit.getChangedAt()).isBetween(beforeExecution, afterExecution);
        }
    }

    @Nested
    @DisplayName("Operation Type Logic Tests")
    class OperationTypeLogicTests {

        @Test
        @DisplayName("Should use DELETE operation type only for REVOKED consent")
        void shouldUseDeleteOperationTypeOnlyForRevokedConsent() {
            Consent revokedConsent = createTestConsent(ConsentStatusEnum.REVOKED);

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(revokedConsent);

            assertThat(audit.getOperationType()).isEqualTo(OperationTypeEnum.DELETE);
        }

        @Test
        @DisplayName("Should use PUT operation type for all non-REVOKED consents")
        void shouldUsePutOperationTypeForAllNonRevokedConsents() {
            Consent activeConsent = createTestConsent(ConsentStatusEnum.ACTIVE);
            Consent expiredConsent = createTestConsent(ConsentStatusEnum.EXPIRED);

            ConsentAudit activeAudit = consentAuditDomainService.createConsentAudit(activeConsent);
            ConsentAudit expiredAudit = consentAuditDomainService.createConsentAudit(expiredConsent);

            assertThat(activeAudit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
            assertThat(expiredAudit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
        }

        @Test
        @DisplayName("Should handle consent status transition in operation type logic")
        void shouldHandleConsentStatusTransitionInOperationTypeLogic() {
            Consent consent = createTestConsent(ConsentStatusEnum.ACTIVE);

            ConsentAudit firstAudit = consentAuditDomainService.createConsentAudit(consent);

            consent.setStatus(ConsentStatusEnum.REVOKED);

            ConsentAudit secondAudit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(firstAudit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
            assertThat(secondAudit.getOperationType()).isEqualTo(OperationTypeEnum.DELETE);
        }
    }

    @Nested
    @DisplayName("Audit Data Consistency Tests")
    class AuditDataConsistencyTests {

        @Test
        @DisplayName("Should maintain data consistency between consent and audit")
        void shouldMaintainDataConsistencyBetweenConsentAndAudit() {
            UUID consentId = UUID.randomUUID();
            Consent consent = new Consent();
            consent.setId(consentId);
            consent.setCpf("99988877766");
            consent.setStatus(ConsentStatusEnum.ACTIVE);
            consent.setCreationDateTime(LocalDateTime.now().minusHours(2));
            consent.setExpirationDateTime(LocalDateTime.now().plusDays(60));
            consent.setAdditionalInfo("Consistency test");

            ConsentAudit audit = consentAuditDomainService.createConsentAudit(consent);

            assertThat(audit.getConsentId()).isEqualTo(consent.getId());
            assertThat(audit.getCpf()).isEqualTo(consent.getCpf());
            assertThat(audit.getStatus()).isEqualTo(consent.getStatus());
            assertThat(audit.getCreationDateTime()).isEqualTo(consent.getCreationDateTime());
            assertThat(audit.getExpirationDateTime()).isEqualTo(consent.getExpirationDateTime());
            assertThat(audit.getAdditionalInfo()).isEqualTo(consent.getAdditionalInfo());
        }
    }

    private Consent createTestConsent(ConsentStatusEnum status) {
        Consent consent = new Consent();
        consent.setId(UUID.randomUUID());
        consent.setCpf("12345678900");
        consent.setStatus(status);
        consent.setCreationDateTime(LocalDateTime.now().minusDays(1));
        consent.setExpirationDateTime(LocalDateTime.now().plusDays(30));
        consent.setAdditionalInfo("Test additional info");
        return consent;
    }
}
