package com.davi.challenge.consents.domain.entity;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import com.davi.challenge.consents.domain.enums.OperationTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ConsentAudit Domain Tests")
public class ConsentAuditTest {

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should maintain all properties correctly")
        void shouldMaintainAllPropertiesCorrectly() {
            UUID id = UUID.randomUUID();
            UUID consentId = UUID.randomUUID();
            String cpf = "12345678900";
            ConsentStatusEnum status = ConsentStatusEnum.ACTIVE;
            LocalDateTime creationDateTime = LocalDateTime.now();
            LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(30);
            String additionalInfo = "Test additional info";
            OperationTypeEnum operationType = OperationTypeEnum.PUT;
            LocalDateTime changedAt = LocalDateTime.now();

            ConsentAudit consentAudit = new ConsentAudit();
            consentAudit.setId(id);
            consentAudit.setConsentId(consentId);
            consentAudit.setCpf(cpf);
            consentAudit.setStatus(status);
            consentAudit.setCreationDateTime(creationDateTime);
            consentAudit.setExpirationDateTime(expirationDateTime);
            consentAudit.setAdditionalInfo(additionalInfo);
            consentAudit.setOperationType(operationType);
            consentAudit.setChangedAt(changedAt);

            assertThat(consentAudit.getId()).isEqualTo(id);
            assertThat(consentAudit.getConsentId()).isEqualTo(consentId);
            assertThat(consentAudit.getCpf()).isEqualTo(cpf);
            assertThat(consentAudit.getStatus()).isEqualTo(status);
            assertThat(consentAudit.getCreationDateTime()).isEqualTo(creationDateTime);
            assertThat(consentAudit.getExpirationDateTime()).isEqualTo(expirationDateTime);
            assertThat(consentAudit.getAdditionalInfo()).isEqualTo(additionalInfo);
            assertThat(consentAudit.getOperationType()).isEqualTo(operationType);
            assertThat(consentAudit.getChangedAt()).isEqualTo(changedAt);
        }

    }

    @Nested
    @DisplayName("Status Enum Compatibility Tests")
    class StatusEnumCompatibilityTests {

        @ParameterizedTest
        @EnumSource(ConsentStatusEnum.class)
        @DisplayName("Should accept all ConsentStatusEnum values")
        void shouldAcceptAllConsentStatusEnumValues(ConsentStatusEnum status) {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setStatus(status);

            assertThat(consentAudit.getStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("Should handle ACTIVE status correctly")
        void shouldHandleActiveStatusCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setStatus(ConsentStatusEnum.ACTIVE);

            assertThat(consentAudit.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("Should handle REVOKED status correctly")
        void shouldHandleRevokedStatusCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setStatus(ConsentStatusEnum.REVOKED);

            assertThat(consentAudit.getStatus()).isEqualTo(ConsentStatusEnum.REVOKED);
        }

        @Test
        @DisplayName("Should handle EXPIRED status correctly")
        void shouldHandleExpiredStatusCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setStatus(ConsentStatusEnum.EXPIRED);

            assertThat(consentAudit.getStatus()).isEqualTo(ConsentStatusEnum.EXPIRED);
        }
    }

    @Nested
    @DisplayName("Operation Type Enum Compatibility Tests")
    class OperationTypeEnumCompatibilityTests {

        @ParameterizedTest
        @EnumSource(OperationTypeEnum.class)
        @DisplayName("Should accept all OperationTypeEnum values")
        void shouldAcceptAllOperationTypeEnumValues(OperationTypeEnum operationType) {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setOperationType(operationType);

            assertThat(consentAudit.getOperationType()).isEqualTo(operationType);
        }

        @Test
        @DisplayName("Should handle PUT operation type correctly")
        void shouldHandlePutOperationTypeCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setOperationType(OperationTypeEnum.PUT);

            assertThat(consentAudit.getOperationType()).isEqualTo(OperationTypeEnum.PUT);
        }

        @Test
        @DisplayName("Should handle DELETE operation type correctly")
        void shouldHandleDeleteOperationTypeCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setOperationType(OperationTypeEnum.DELETE);

            assertThat(consentAudit.getOperationType()).isEqualTo(OperationTypeEnum.DELETE);
        }
    }

    @Nested
    @DisplayName("UUID Handling Tests")
    class UuidHandlingTests {

        @Test
        @DisplayName("Should handle different UUID values for id and consentId")
        void shouldHandleDifferentUuidValuesForIdAndConsentId() {
            UUID auditId = UUID.randomUUID();
            UUID consentId = UUID.randomUUID();
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setId(auditId);
            consentAudit.setConsentId(consentId);

            assertThat(consentAudit.getId()).isEqualTo(auditId);
            assertThat(consentAudit.getConsentId()).isEqualTo(consentId);
            assertThat(consentAudit.getId()).isNotEqualTo(consentAudit.getConsentId());
        }
    }

    @Nested
    @DisplayName("DateTime Handling Tests")
    class DateTimeHandlingTests {

        @Test
        @DisplayName("Should handle different datetime values correctly")
        void shouldHandleDifferentDatetimeValuesCorrectly() {
            LocalDateTime creationDateTime = LocalDateTime.now().minusDays(1);
            LocalDateTime changedAt = LocalDateTime.now();
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setCreationDateTime(creationDateTime);
            consentAudit.setChangedAt(changedAt);

            assertThat(consentAudit.getCreationDateTime()).isNotEqualTo(changedAt);
            assertThat(consentAudit.getChangedAt()).isNotEqualTo(creationDateTime);
        }

        @Test
        @DisplayName("Should handle null expiration date correctly")
        void shouldHandleNullExpirationDateCorrectly() {
            ConsentAudit consentAudit = new ConsentAudit();
            LocalDateTime creationDateTime = LocalDateTime.now();
            LocalDateTime changedAt = LocalDateTime.now();

            consentAudit.setCreationDateTime(creationDateTime);
            consentAudit.setExpirationDateTime(null);
            consentAudit.setChangedAt(changedAt);

            assertThat(consentAudit.getCreationDateTime()).isEqualTo(creationDateTime);
            assertThat(consentAudit.getExpirationDateTime()).isNull();
            assertThat(consentAudit.getChangedAt()).isEqualTo(changedAt);
        }
    }

    @Nested
    @DisplayName("Audit Context Tests")
    class AuditContextTests {

        @Test
        @DisplayName("Should maintain relationship with original consent")
        void shouldMaintainRelationshipWithOriginalConsent() {
            UUID originalConsentId = UUID.randomUUID();
            String originalCpf = "12345678900";
            ConsentAudit consentAudit = new ConsentAudit();

            consentAudit.setConsentId(originalConsentId);
            consentAudit.setCpf(originalCpf);

            assertThat(consentAudit.getConsentId()).isEqualTo(originalConsentId);
            assertThat(consentAudit.getCpf()).isEqualTo(originalCpf);
        }
    }
}
