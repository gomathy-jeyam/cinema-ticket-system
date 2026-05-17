package uk.gov.dwp.engineering.recruitment.booking.calculator;

import uk.gov.dwp.engineering.recruitment.domain.Booking;

public interface SeatCalculator {
    long calculate(Booking booking);
}
