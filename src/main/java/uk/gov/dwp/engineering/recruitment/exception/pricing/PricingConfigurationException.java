package uk.gov.dwp.engineering.recruitment.exception.pricing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PricingConfigurationException extends RuntimeException {
    public PricingConfigurationException(String message) {
        super(message);
    }
}