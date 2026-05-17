package uk.gov.dwp.engineering.recruitment.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidBookingExceptionTest {

    @Test
    void shouldReturnMessage_whenInvalidBookingExceptionCreated() {
        InvalidBookingException exception = new InvalidBookingException("Invalid booking");

        assertThat(exception.getMessage()).isEqualTo("Invalid booking");
    }

    @Test
    void shouldBeRuntimeException_whenInvalidBookingExceptionCreated() {
        InvalidBookingException exception = new InvalidBookingException("Invalid booking");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
