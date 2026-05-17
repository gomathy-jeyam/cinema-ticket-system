package uk.gov.dwp.engineering.recruitment.booking.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.dwp.engineering.recruitment.booking.validation.*;

import java.util.List;

@Configuration
public class ValidationConfiguration {

    @Bean
    public List<ValidationRule> validationRules(
            @Qualifier("accountValidationRule") ValidationRule accountValidation,
            @Qualifier("ticketRequestValidationRule") ValidationRule ticketRequestValidationRule,
            @Qualifier("maximumTicketsValidationRule") ValidationRule maximumTicketsValidation,
            @Qualifier("adultPresenceValidationRule") ValidationRule adultPresenceValidation){
        return List.of(accountValidation, ticketRequestValidationRule, maximumTicketsValidation, adultPresenceValidation);
    }
}
