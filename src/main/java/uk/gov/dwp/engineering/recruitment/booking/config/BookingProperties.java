package uk.gov.dwp.engineering.recruitment.booking.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Validated
@ConfigurationProperties(prefix = "booking")
public record BookingProperties(

        @NotNull(message = "booking.max-tickets-per-purchase must be configured")
        @Positive(message = "booking.max-tickets-per-purchase must be greater than zero")
        Integer maxTicketsPerPurchase,

        @Valid
        @NotNull(message = "booking.pricing must be configured")
        Pricing pricing

) {

    public record Pricing(

            @NotNull(message = "booking.pricing.adult must be configured")
            @DecimalMin(value = "0.0", inclusive = true,
                    message = "booking.pricing.adult must be >= 0")
            BigDecimal adult,

            @NotNull(message = "booking.pricing.child must be configured")
            @DecimalMin(value = "0.0", inclusive = true,
                    message = "booking.pricing.child must be >= 0")
            BigDecimal child,

            @NotNull(message = "booking.pricing.infant must be configured")
            @DecimalMin(value = "0.0", inclusive = true,
                    message = "booking.pricing.infant must be >= 0")
            BigDecimal infant

    ) {
    }
}
