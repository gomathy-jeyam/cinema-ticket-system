package uk.gov.dwp.engineering.recruitment.booking.validation;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.exception.validation.InvalidAccountException;

@Component
public class AccountValidationRule implements ValidationRule {
    @Override
    public void validate(final Booking booking) {
        if(booking.accountId() == null || booking.accountId() <=0){
            throw new InvalidAccountException("Account id should be greater than 0");
        }
    }
}
