package uk.gov.dwp.engineering.recruitment.booking.validation;

import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;

public interface ValidationRule {
    void validate(Booking booking);
}
