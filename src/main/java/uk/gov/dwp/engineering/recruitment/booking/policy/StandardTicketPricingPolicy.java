package uk.gov.dwp.engineering.recruitment.booking.policy;

import org.springframework.stereotype.Component;
import uk.gov.dwp.engineering.recruitment.booking.config.BookingProperties;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.pricing.PricingConfigurationException;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

@Component
public class StandardTicketPricingPolicy implements TicketPricingPolicy {
    private final Map<TicketType, BigDecimal> prices;

    public StandardTicketPricingPolicy(BookingProperties properties){
        BookingProperties.Pricing pricing = properties.pricing();
        prices = Map.copyOf(new EnumMap<>(Map.of(
                TicketType.ADULT, pricing.adult(),
                TicketType.CHILD, pricing.child(),
                TicketType.INFANT, pricing.infant()
        )));
    }

    @Override
    public BigDecimal priceFor(TicketType ticketType){
        Objects.requireNonNull(ticketType, "Ticket type must not be null");
        BigDecimal price = prices.get(ticketType);
        if(price == null){
            throw new PricingConfigurationException("No pricing configured for ticket type: "+ ticketType);
        }
        return price;
    }
}
