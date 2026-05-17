package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.validation.InsufficientAdultTicketsException;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.stream.Collectors;

@Component
public class AdultPresenceValidationRule implements ValidationRule{
    @Override
    public void validate(final Booking booking) {
        EnumMap<TicketType, Integer> countByType = Arrays.stream(booking.ticketRequests())
                .collect(Collectors.toMap(TicketRequest::type, TicketRequest::ticketCount, Integer::sum, ()-> new EnumMap<>(TicketType.class)));

        if(!countByType.containsKey(TicketType.ADULT) && (countByType.containsKey(TicketType.CHILD)|| countByType.containsKey(TicketType.INFANT))){
            throw new InsufficientAdultTicketsException("Child and Infant tickets cannot be purchased without at least one Adult ticket");
        }

        int adultTicket = countByType.getOrDefault(TicketType.ADULT, 0);
        int infantTicket = countByType.getOrDefault(TicketType.INFANT, 0);

        if(infantTicket > adultTicket){
            throw new InsufficientAdultTicketsException("Each infant ticket requires a corresponding adult ticket");
        }
    }
}
