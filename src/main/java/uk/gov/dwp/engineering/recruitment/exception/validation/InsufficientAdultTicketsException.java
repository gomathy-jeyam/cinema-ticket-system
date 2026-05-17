package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InsufficientAdultTicketsException extends PurchaseValidationException{
    public InsufficientAdultTicketsException(String message) {
        super(message);
    }
}
