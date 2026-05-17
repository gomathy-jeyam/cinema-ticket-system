package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InputValidationExceptionTest {

    @Test
    void shouldReturnMessage_whenInputValidationExceptionCreated() {
        InputValidationException exception = new InputValidationException("Ticket requests should not be empty");

        assertThat(exception.getMessage()).isEqualTo("Ticket requests should not be empty");
    }

    @Test
    void shouldBePurchaseValidationException_whenInputValidationExceptionCreated() {
        InputValidationException exception = new InputValidationException("invalid input");

        assertThat(exception).isInstanceOf(PurchaseValidationException.class);
    }

    @Test
    void shouldBeRuntimeException_whenInputValidationExceptionCreated() {
        InputValidationException exception = new InputValidationException("invalid input");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
