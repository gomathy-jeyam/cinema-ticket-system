package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.exception.validation.InputValidationException;

import java.util.Arrays;

@Component
public class TicketRequestValidationRule implements ValidationRule{
    @Override
    public void validate(final Booking booking) {
        TicketRequest[] ticketRequests = booking.ticketRequests();
        if(ticketRequests ==null || ticketRequests.length==0){
            throw new InputValidationException("Ticket requests should not be empty");
        }

        for (TicketRequest ticketRequest : ticketRequests) {
            if (ticketRequest == null || ticketRequest.type() == null) {
                throw new InputValidationException(
                        "Ticket requests must not contain null entries or null ticket types");
            }

            if (ticketRequest.ticketCount() <= 0) {
                throw new InputValidationException(
                        "Each ticket request must specify at least one ticket");
            }
        }
    }
}
