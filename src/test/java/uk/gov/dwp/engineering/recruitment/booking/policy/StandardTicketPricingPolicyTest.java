package uk.gov.dwp.engineering.recruitment.booking.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.booking.config.BookingProperties;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StandardTicketPricingPolicyTest {

    private StandardTicketPricingPolicy policy;

    @BeforeEach
    void setUp() {
        BookingProperties.Pricing pricing = new BookingProperties.Pricing(
                new BigDecimal("25"),
                new BigDecimal("15"),
                BigDecimal.ZERO);
        policy = new StandardTicketPricingPolicy(new BookingProperties(25, pricing));
    }

    @Test
    void shouldReturnAdultPrice_whenAdultTicketTypeRequested() {
        BigDecimal price = policy.priceFor(TicketType.ADULT);

        assertThat(price).isEqualByComparingTo("25");
    }

    @Test
    void shouldReturnChildPrice_whenChildTicketTypeRequested() {
        BigDecimal price = policy.priceFor(TicketType.CHILD);

        assertThat(price).isEqualByComparingTo("15");
    }

    @Test
    void shouldReturnZeroPrice_whenInfantTicketTypeRequested() {
        BigDecimal price = policy.priceFor(TicketType.INFANT);

        assertThat(price).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldSupportDynamicConfiguredPrices_whenCustomPricingProvided() {
        BookingProperties.Pricing customPricing = new BookingProperties.Pricing(
                new BigDecimal("30"), new BigDecimal("20"), BigDecimal.ZERO);
        StandardTicketPricingPolicy customPolicy =
                new StandardTicketPricingPolicy(new BookingProperties(25, customPricing));

        assertThat(customPolicy.priceFor(TicketType.ADULT)).isEqualByComparingTo("30");
        assertThat(customPolicy.priceFor(TicketType.CHILD)).isEqualByComparingTo("20");
    }

    @Test
    void shouldThrowNullPointerException_whenNullTicketTypeRequested() {
        assertThatThrownBy(() -> policy.priceFor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Ticket type must not be null");
    }
}
