package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.validation.InsufficientAdultTicketsException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdultPresenceValidationRuleTest {

    private AdultPresenceValidationRule validator;

    private static final Long ACCOUNT_ID = 101L;

    @BeforeEach
    void setUp() {
        validator = new AdultPresenceValidationRule();
    }

    @Test
    void shouldPassValidation_whenOnlyAdultTickets() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenMultipleAdultTickets() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 5));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenMultipleDuplicateAdultTickets() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.ADULT, 2));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenAdultAndChildTickets() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.CHILD, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenOneAdultAndManyChildTickets() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.CHILD, 5));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenAdultCountEqualsInfantCount() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.INFANT, 2));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenMoreAdultsThanInfants() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 3),
                new TicketRequest(TicketType.INFANT, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenAdultChildAndInfantMix() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.CHILD, 3),
                new TicketRequest(TicketType.INFANT, 2));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowInsufficientAdultTicketsException_whenOnlyChildTickets() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.CHILD, 1));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InsufficientAdultTicketsException.class)
                .hasMessage("Child and Infant tickets cannot be purchased without at least one Adult ticket");
    }

    @Test
    void shouldThrowInsufficientAdultTicketsException_whenOnlyInfantTickets() {
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.INFANT, 1));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InsufficientAdultTicketsException.class)
                .hasMessage("Child and Infant tickets cannot be purchased without at least one Adult ticket");
    }

    @Test
    void shouldThrowInsufficientAdultTicketsException_whenChildAndInfantWithNoAdult() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.CHILD, 2),
                new TicketRequest(TicketType.INFANT, 1));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InsufficientAdultTicketsException.class);
    }

    @Test
    void shouldThrowInsufficientAdultTicketsException_whenMoreInfantsThanAdults() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.INFANT, 2));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InsufficientAdultTicketsException.class)
                .hasMessage("Each infant ticket requires a corresponding adult ticket");
    }

    @Test
    void shouldThrowInsufficientAdultTicketsException_whenTwoAdultsAndThreeInfants() {
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.INFANT, 3));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(InsufficientAdultTicketsException.class);
    }
}
