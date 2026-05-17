package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.booking.config.BookingProperties;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.exception.validation.TicketLimitExceededException;

import java.util.Arrays;

@Component
public class MaximumTicketsValidationRule implements ValidationRule{
    private final int maxTicketsPerPurchase;

    public MaximumTicketsValidationRule(BookingProperties properties) {
        this.maxTicketsPerPurchase = properties.maxTicketsPerPurchase();
    }

    @Override
    public void validate(final Booking booking) {
        int totalTickets = Arrays.stream(booking.ticketRequests()).mapToInt(TicketRequest::ticketCount).sum();

        if(totalTickets > maxTicketsPerPurchase){
            throw new TicketLimitExceededException(
                    String.format("Cannot purchase more than %d tickets per transaction", maxTicketsPerPurchase ));
        }
    }

}
