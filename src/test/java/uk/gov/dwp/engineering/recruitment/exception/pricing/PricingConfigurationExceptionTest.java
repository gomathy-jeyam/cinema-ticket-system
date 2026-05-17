package uk.gov.dwp.engineering.recruitment.exception.pricing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PricingConfigurationExceptionTest {

    @Test
    void shouldReturnMessage_whenPricingConfigurationExceptionCreated() {
        PricingConfigurationException exception = new PricingConfigurationException("No pricing configured for ticket type: X");

        assertThat(exception.getMessage()).isEqualTo("No pricing configured for ticket type: X");
    }

    @Test
    void shouldBeRuntimeException_whenPricingConfigurationExceptionCreated() {
        PricingConfigurationException exception = new PricingConfigurationException("pricing error");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}
