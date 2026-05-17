package uk.gov.dwp.engineering.recruitment.booking.presentation;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.domain.Booking;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public final class BookingReceiptFormatter {

    public String build(
            final Booking booking,
            final long seatsCount,
            final BigDecimal totalPrice) {

        String template = """
                -----------------
                Booking confirmed
                -----------------
                Account: %d
                Tickets: [%s]
                Seats reserved: %d
                Total charged: £%.2f
                """;

        String ticketLine = Arrays.stream(booking.ticketRequests())
                .map(ticketRequest ->
                        ticketRequest.ticketCount() + " x " + ticketRequest.type())
                .collect(Collectors.joining(", "));

        return template.formatted(
                booking.accountId(),
                ticketLine,
                seatsCount,
                totalPrice
        );
    }
}
