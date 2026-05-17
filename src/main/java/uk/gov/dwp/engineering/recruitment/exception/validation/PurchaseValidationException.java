package uk.gov.dwp.engineering.recruitment.exception.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public abstract class PurchaseValidationException extends RuntimeException{
    public PurchaseValidationException(final String message){
        super(message);
    }
}
