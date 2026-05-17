package uk.gov.dwp.engineering.recruitment.booking.policy;

import uk.gov.dwp.engineering.recruitment.domain.TicketType;

public interface SeatReservationPolicy {
    boolean requiresSeat(TicketType ticketType);
}
