package uk.gov.dwp.engineering.recruitment.booking.calculator;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.booking.policy.TicketPricingPolicy;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;

import java.math.BigDecimal;
import java.util.Arrays;

@Component
public class StandardPriceCalculator implements  PriceCalculator{
    private final TicketPricingPolicy ticketPricingPolicy;

    public StandardPriceCalculator(TicketPricingPolicy ticketPricingPolicy) {
        this.ticketPricingPolicy = ticketPricingPolicy;
    }

    @Override
    public BigDecimal calculate(final Booking booking){
        return Arrays.stream(booking.ticketRequests())
                .map(ticketRequest ->
                        ticketPricingPolicy.priceFor(ticketRequest.type())
                        .multiply(BigDecimal.valueOf(ticketRequest.ticketCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
