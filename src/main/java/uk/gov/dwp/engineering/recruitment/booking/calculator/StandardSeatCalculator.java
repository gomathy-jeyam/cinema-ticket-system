package uk.gov.dwp.engineering.recruitment.booking.calculator;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.booking.policy.SeatReservationPolicy;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;

import java.util.Arrays;

@Component
public class StandardSeatCalculator implements SeatCalculator{
    private final SeatReservationPolicy seatReservationPolicy;

    public StandardSeatCalculator(SeatReservationPolicy seatReservationPolicy) {
        this.seatReservationPolicy = seatReservationPolicy;
    }

    @Override
    public long calculate(final Booking booking){
        return Arrays.stream(booking.ticketRequests())
                .filter(ticketRequest -> seatReservationPolicy.requiresSeat(ticketRequest.type()))
                .mapToLong(TicketRequest::ticketCount)
                .sum();
    }
}
