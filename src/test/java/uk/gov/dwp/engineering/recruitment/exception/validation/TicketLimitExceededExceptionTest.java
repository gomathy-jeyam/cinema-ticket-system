package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TicketLimitExceededExceptionTest {

    @Test
    void shouldReturnMessage_whenTicketLimitExceededExceptionCreated() {
        TicketLimitExceededException exception = new TicketLimitExceededException(
                "Cannot purchase more than 25 tickets per transaction");

        assertThat(exception.getMessage())
                .isEqualTo("Cannot purchase more than 25 tickets per transaction");
    }

    @Test
    void shouldBePurchaseValidationException_whenTicketLimitExceededExceptionCreated() {
        TicketLimitExceededException exception = new TicketLimitExceededException("limit exceeded");

        assertThat(exception).isInstanceOf(PurchaseValidationException.class);
    }

    @Test
    void shouldBeRuntimeException_whenTicketLimitExceededExceptionCreated() {
        TicketLimitExceededException exception = new TicketLimitExceededException("limit exceeded");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
