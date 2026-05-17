package uk.gov.dwp.engineering.recruitment.exception.validation;

public class TicketLimitExceededException extends PurchaseValidationException{

    public TicketLimitExceededException(String message) {
        super(message);
    }
}
