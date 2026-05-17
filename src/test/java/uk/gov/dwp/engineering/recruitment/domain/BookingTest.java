package uk.gov.dwp.engineering.recruitment.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.engineering.recruitment.domain.TicketType.ADULT;

class BookingTest {

    private final Long accountId = 123L;

    @Test
    void shouldReturnAccountIdAndTicketRequests_whenBookingCreatedWithAdultTicket() {
        TicketRequest ticketRequest = new TicketRequest(ADULT, 1);
        Booking booking = new Booking(accountId, ticketRequest);

        assertThat(booking.accountId()).isEqualTo(accountId);
        assertThat(booking.ticketRequests()).hasSize(1).contains(ticketRequest);
    }

    @Test
    void shouldReturnAccountIdAndTicketRequests_whenBookingCreatedWithNoTicket() {
        Booking booking = new Booking(accountId);

        assertThat(booking.accountId()).isEqualTo(accountId);
        assertThat(booking.ticketRequests()).hasSize(0);
    }

    @Test
    void shouldReturnAccountIdAndMultipleTicketRequests_whenBookingCreatedWithMultipleTicket() {
        Booking booking = new Booking(accountId,
                new TicketRequest(TicketType.INFANT, 2),
                new TicketRequest(TicketType.CHILD, 3));

        assertThat(booking.accountId()).isEqualTo(accountId);
        assertThat(booking.ticketRequests()).hasSize(2);
    }
}
