package uk.gov.dwp.engineering.recruitment.booking.calculator;

import uk.gov.dwp.engineering.recruitment.domain.Booking;

import java.math.BigDecimal;

public interface PriceCalculator {
    BigDecimal calculate(Booking booking);
}
