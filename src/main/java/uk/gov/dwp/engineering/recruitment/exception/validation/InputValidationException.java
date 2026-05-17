package uk.gov.dwp.engineering.recruitment.exception.validation;

public class InputValidationException extends PurchaseValidationException {
    public InputValidationException(final String message){
        super(message);
    }
}
