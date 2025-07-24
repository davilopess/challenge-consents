package com.davi.challenge.consents.domain.entity;

import com.davi.challenge.consents.domain.enums.ConsentStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Consent Domain Tests")
public class ConsentTest {

    @Nested
    @DisplayName("defineStatus() Method Tests")
    class DefineStatusTests {

        @Test
        @DisplayName("Should set status to EXPIRED when expiration date is in the past")
        void shouldSetStatusToExpiredWhenExpirationDateIsInThePast() {
            Consent consent = new Consent();
            consent.setExpirationDateTime(LocalDateTime.now().minusDays(1));

            consent.defineStatus();

            assertThat(consent.getStatus()).isEqualTo(ConsentStatusEnum.EXPIRED);
        }

        @Test
        @DisplayName("Should set status to ACTIVE when expiration date is in the future")
        void shouldSetStatusToActiveWhenExpirationDateIsInTheFuture() {
            Consent consent = new Consent();
            consent.setExpirationDateTime(LocalDateTime.now().plusDays(1));

            consent.defineStatus();

            assertThat(consent.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("Should set status to ACTIVE when expiration date is null")
        void shouldSetStatusToActiveWhenExpirationDateIsNull() {
            Consent consent = new Consent();
            consent.setExpirationDateTime(null);

            consent.defineStatus();

            assertThat(consent.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("Should override existing status when called multiple times")
        void shouldOverrideExistingStatusWhenCalledMultipleTimes() {
            Consent consent = new Consent();
            consent.setStatus(ConsentStatusEnum.REVOKED); // Status inicial
            consent.setExpirationDateTime(LocalDateTime.now().plusDays(1));

            consent.defineStatus();

            assertThat(consent.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
        }

        @Test
        @DisplayName("Should handle edge case with expiration exactly one second in future")
        void shouldHandleEdgeCaseWithExpirationExactlyOneSecondInFuture() {
            Consent consent = new Consent();
            consent.setExpirationDateTime(LocalDateTime.now().plusSeconds(1));

            consent.defineStatus();

            assertThat(consent.getStatus()).isEqualTo(ConsentStatusEnum.ACTIVE);
        }
    }

    @Nested
    @DisplayName("isRevoked() Method Tests")
    class IsRevokedTests {

        @Test
        @DisplayName("Should return true when status is REVOKED")
        void shouldReturnTrueWhenStatusIsRevoked() {
            Consent consent = new Consent();
            consent.setStatus(ConsentStatusEnum.REVOKED);

            boolean result = consent.isRevoked();

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should return false when status is ACTIVE")
        void shouldReturnFalseWhenStatusIsActive() {
            Consent consent = new Consent();
            consent.setStatus(ConsentStatusEnum.ACTIVE);

            boolean result = consent.isRevoked();

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should return false when status is EXPIRED")
        void shouldReturnFalseWhenStatusIsExpired() {
            Consent consent = new Consent();
            consent.setStatus(ConsentStatusEnum.EXPIRED);

            boolean result = consent.isRevoked();

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should return false when status is null")
        void shouldReturnFalseWhenStatusIsNull() {
            Consent consent = new Consent();
            consent.setStatus(null);

            boolean result = consent.isRevoked();

            assertThat(result).isFalse();
        }

        @ParameterizedTest
        @EnumSource(value = ConsentStatusEnum.class, names = {"ACTIVE", "EXPIRED"})
        @DisplayName("Should return false for all non-REVOKED statuses")
        void shouldReturnFalseForAllNonRevokedStatuses(ConsentStatusEnum status) {
            Consent consent = new Consent();
            consent.setStatus(status);

            boolean result = consent.isRevoked();

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should maintain all properties correctly")
        void shouldMaintainAllPropertiesCorrectly() {
            UUID id = UUID.randomUUID();
            String cpf = "12345678900";
            ConsentStatusEnum status = ConsentStatusEnum.ACTIVE;
            LocalDateTime creationDateTime = LocalDateTime.now();
            LocalDateTime expirationDateTime = LocalDateTime.now().plusDays(30);
            String additionalInfo = "Test additional info";

            Consent consent = new Consent();
            consent.setId(id);
            consent.setCpf(cpf);
            consent.setStatus(status);
            consent.setCreationDateTime(creationDateTime);
            consent.setExpirationDateTime(expirationDateTime);
            consent.setAdditionalInfo(additionalInfo);

            assertThat(consent.getId()).isEqualTo(id);
            assertThat(consent.getCpf()).isEqualTo(cpf);
            assertThat(consent.getStatus()).isEqualTo(status);
            assertThat(consent.getCreationDateTime()).isEqualTo(creationDateTime);
            assertThat(consent.getExpirationDateTime()).isEqualTo(expirationDateTime);
            assertThat(consent.getAdditionalInfo()).isEqualTo(additionalInfo);
        }
    }
}
