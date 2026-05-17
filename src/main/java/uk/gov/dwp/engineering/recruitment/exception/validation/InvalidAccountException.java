package uk.gov.dwp.engineering.recruitment.exception.validation;

public class InvalidAccountException extends PurchaseValidationException{

    public InvalidAccountException(String message) {
        super(message);
    }
}
