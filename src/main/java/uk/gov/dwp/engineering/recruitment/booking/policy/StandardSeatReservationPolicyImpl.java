package uk.gov.dwp.engineering.recruitment.booking.policy;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

@Component
public class StandardSeatReservationPolicyImpl implements SeatReservationPolicy{
    @Override
    public boolean requiresSeat(TicketType ticketType) {
        return ticketType!=TicketType.INFANT;
    }
}
