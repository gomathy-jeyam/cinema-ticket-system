package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.booking.config.BookingProperties;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.validation.TicketLimitExceededException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MaximumTicketsValidationRuleTest {

    private static final Long ACCOUNT_ID = 123L;

    private MaximumTicketsValidationRule validatorWithLimit(int limit) {
        BookingProperties.Pricing pricing = new BookingProperties.Pricing(
                BigDecimal.valueOf(25), BigDecimal.valueOf(15), BigDecimal.ZERO);
        return new MaximumTicketsValidationRule(new BookingProperties(limit, pricing));
    }

    @Test
    void shouldPassValidation_whenTotalTicketsEqualToLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 25));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenSingleTicketBelowLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 1));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldPassValidation_whenMultipleTypesWhoseTotalIsAtLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 10),
                new TicketRequest(TicketType.CHILD, 10),
                new TicketRequest(TicketType.INFANT, 5));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowTicketLimitExceededException_whenTotalIsOneOverLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 26));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(TicketLimitExceededException.class)
                .hasMessageContaining("25");
    }

    @Test
    void shouldThrowTicketLimitExceededException_whenTotalFarExceedsLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 100));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(TicketLimitExceededException.class);
    }

    @Test
    void shouldThrowTicketLimitExceededException_whenCombinedTypesExceedLimit() {
        MaximumTicketsValidationRule validator = validatorWithLimit(25);
        Booking booking = new Booking(ACCOUNT_ID,
                new TicketRequest(TicketType.ADULT, 15),
                new TicketRequest(TicketType.CHILD, 11));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(TicketLimitExceededException.class);
    }

    @Test
    void shouldThrowTicketLimitExceededException_whenCustomLimitOfOneIsExceeded() {
        MaximumTicketsValidationRule validator = validatorWithLimit(1);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 2));

        assertThatThrownBy(() -> validator.validate(booking))
                .isInstanceOf(TicketLimitExceededException.class)
                .hasMessageContaining("1");
    }

    @Test
    void shouldPassValidation_whenCustomLimitMatchedExactly() {
        MaximumTicketsValidationRule validator = validatorWithLimit(5);
        Booking booking = new Booking(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 5));

        assertThatCode(() -> validator.validate(booking)).doesNotThrowAnyException();
    }
}
