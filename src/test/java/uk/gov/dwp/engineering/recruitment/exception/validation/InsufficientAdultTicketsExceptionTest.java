package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InsufficientAdultTicketsExceptionTest {

    @Test
    void shouldReturnMessage_whenInsufficientAdultTicketsExceptionCreated() {
        InsufficientAdultTicketsException exception = new InsufficientAdultTicketsException(
                "Child and Infant tickets cannot be purchased without at least one Adult ticket");

        assertThat(exception.getMessage())
                .isEqualTo("Child and Infant tickets cannot be purchased without at least one Adult ticket");
    }

    @Test
    void shouldBePurchaseValidationException_whenInsufficientAdultTicketsExceptionCreated() {
        InsufficientAdultTicketsException exception = new InsufficientAdultTicketsException("no adult present");

        assertThat(exception).isInstanceOf(PurchaseValidationException.class);
    }

    @Test
    void shouldBeRuntimeException_whenInsufficientAdultTicketsExceptionCreated() {
        InsufficientAdultTicketsException exception = new InsufficientAdultTicketsException("no adult present");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
