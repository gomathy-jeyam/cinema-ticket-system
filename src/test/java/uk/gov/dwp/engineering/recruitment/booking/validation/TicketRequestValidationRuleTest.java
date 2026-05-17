package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.validation.InputValidationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TicketRequestValidationRuleTest {

    private TicketRequestValidationRule validator;

    private static final Long ACCOUNT_ID = 12345L;

    @BeforeEach
    void setUp() {
        validator = new TicketRequestValidationRule();
    }

    @Test
    void shouldThrowInputValidationException_whenTicketRequestsIsNull() {
        Booking booking = new Booking(ACCOUNT_ID, null);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Ticket requests should not be empty");
    }

    @Test
    void shouldThrowInputValidationException_whenTicketRequestsIsEmpty() {
        Booking booking = new Booking(ACCOUNT_ID);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Ticket requests should not be empty");
    }

    @Test
    void shouldThrowInputValidationException_whenTicketRequestArrayContainsNullEntry() {
        Booking booking = new Booking(ACCOUNT_ID, (TicketRequest) null);

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Ticket requests must not contain null entries or null ticket types");
    }

    @Test
    void shouldThrowInputValidationException_whenTicketRequestHasNullType() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(null, 1));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Ticket requests must not contain null entries or null ticket types");
    }

    @Test
    void shouldThrowInputValidationException_whenTicketCountIsZero() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 0));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Each ticket request must specify at least one ticket");
    }

    @Test
    void shouldThrowInputValidationException_whenTicketCountIsNegative() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, -1));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class)
                .hasMessage("Each ticket request must specify at least one ticket");
    }

    @Test
    void shouldThrowInputValidationException_whenOneOfMultipleEntriesHasZeroCount() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.CHILD, 0));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InputValidationException.class);
    }

    @Test
    void shouldPassValidation_whenSingleAdultTicketWithValidCount() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenMultipleValidTicketTypes() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.CHILD, 3),
                new TicketRequest(TicketType.INFANT, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }
}
