package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.validation.InvalidAccountException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountValidationRuleTest {

    private AccountValidationRule validator;

    private static final TicketRequest ONE_ADULT = new TicketRequest(TicketType.ADULT, 1);

    @BeforeEach
    void setUp() {
        validator = new AccountValidationRule();
    }

    @Test
    void shouldThrowInvalidAccountException_whenAccountIdIsNull() {
        Booking booking = new Booking(null, ONE_ADULT);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InvalidAccountException.class)
                .hasMessage("Account id should be greater than 0");
    }

    @Test
    void shouldThrowInvalidAccountException_whenAccountIdIsZero() {
        Booking booking = new Booking(0L, ONE_ADULT);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InvalidAccountException.class)
                .hasMessage("Account id should be greater than 0");
    }

    @Test
    void shouldThrowInvalidAccountException_whenAccountIdIsNegative() {
        Booking booking = new Booking(-1L, ONE_ADULT);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InvalidAccountException.class);
    }

    @Test
    void shouldThrowInvalidAccountException_whenAccountIdIsLargeNegative() {
        Booking booking = new Booking(Long.MIN_VALUE, ONE_ADULT);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InvalidAccountException.class);
    }

    @Test
    void shouldPassValidation_whenAccountIdIsOne() {
        Booking booking = new Booking(1L, ONE_ADULT);

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenAccountIdIsLargePositive() {
        Booking booking = new Booking(Long.MAX_VALUE, ONE_ADULT);

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenAccountIdIsUsualCustomerValue() {
        Booking booking = new Booking(12345L, ONE_ADULT);

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }
}
