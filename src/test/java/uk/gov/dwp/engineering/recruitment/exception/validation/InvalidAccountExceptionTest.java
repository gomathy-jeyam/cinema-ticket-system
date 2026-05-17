package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidAccountExceptionTest {

    @Test
    void shouldReturnMessage_whenInvalidAccountExceptionCreated() {
        InvalidAccountException exception = new InvalidAccountException("Account id should be greater than 0");

        assertThat(exception.getMessage()).isEqualTo("Account id should be greater than 0");
    }

    @Test
    void shouldBePurchaseValidationException_whenInvalidAccountExceptionCreated() {
        InvalidAccountException exception = new InvalidAccountException("invalid account");

        assertThat(exception).isInstanceOf(PurchaseValidationException.class);
    }

    @Test
    void shouldBeRuntimeException_whenInvalidAccountExceptionCreated() {
        InvalidAccountException exception = new InvalidAccountException("invalid account");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
