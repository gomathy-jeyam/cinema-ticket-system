package uk.gov.dwp.engineering.recruitment.booking.policy;

import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.pricing.PricingConfigurationException;

import java.math.BigDecimal;

public interface TicketPricingPolicy {
    BigDecimal priceFor(TicketType ticketType);
}
