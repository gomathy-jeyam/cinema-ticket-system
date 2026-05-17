package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseValidationExceptionTest {

    private PurchaseValidationException exceptionWithMessage(String message) {
        return new PurchaseValidationException(message) {};
    }

    @Test
    void shouldReturnMessage_whenPurchaseValidationExceptionCreated() {
        PurchaseValidationException exception = exceptionWithMessage("validation failed");

        assertThat(exception.getMessage()).isEqualTo("validation failed");
    }

    @Test
    void shouldBeRuntimeException_whenPurchaseValidationExceptionCreated() {
        PurchaseValidationException exception = exceptionWithMessage("validation failed");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
