package uk.gov.dwp.engineering.recruitment.booking.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.engineering.recruitment.booking.policy.TicketPricingPolicy;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardPriceCalculatorTest {

    @Mock
    private TicketPricingPolicy pricingPolicy;

    private StandardPriceCalculator calculator;

    private static final BigDecimal ADULT_PRICE  = new BigDecimal("25");
    private static final BigDecimal CHILD_PRICE  = new BigDecimal("15");
    private static final BigDecimal INFANT_PRICE = BigDecimal.ZERO;

    @BeforeEach
    void setUp() {
        calculator = new StandardPriceCalculator(pricingPolicy);
    }

    private void stubAdultPrice() {
        when(pricingPolicy.priceFor(TicketType.ADULT)).thenReturn(ADULT_PRICE);
    }

    private void stubChildPrice() {
        when(pricingPolicy.priceFor(TicketType.CHILD)).thenReturn(CHILD_PRICE);
    }

    private void stubInfantPrice() {
        when(pricingPolicy.priceFor(TicketType.INFANT)).thenReturn(INFANT_PRICE);
    }

    @Test
    void shouldReturnZero_whenNoTickets() {
        Booking booking = new Booking(1L);

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateCorrectTotal_whenSingleAdultTicketPurchased() {
        stubAdultPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.ADULT, 1));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("25");
    }

    @Test
    void shouldCalculateCorrectTotal_whenMultipleAdultTickets() {
        stubAdultPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.ADULT, 3));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("75");
    }

    @Test
    void shouldCalculateCorrectTotal_whenSingleChildTicket() {
        stubChildPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.CHILD, 1));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("15");
    }

    @Test
    void shouldCalculateCorrectTotal_whenMultipleChildTickets() {
        stubChildPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.CHILD, 4));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("60");
    }

    @Test
    void shouldReturnZero_whenSingleInfantTicket() {
        stubInfantPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.INFANT, 1));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldReturnZero_whenMultipleInfantTickets() {
        stubInfantPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.INFANT, 5));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldCalculateCorrectTotal_whenOneAdultOneChild() {
        stubAdultPrice();
        stubChildPrice();
        Booking booking = new Booking(1L,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.CHILD, 1));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("40");
    }

    @Test
    void shouldCalculateCorrectTotal_whenAdultChildAndInfantMixed() {
        stubAdultPrice();
        stubChildPrice();
        stubInfantPrice();
        Booking booking = new Booking(1L,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.CHILD, 1),
                new TicketRequest(TicketType.INFANT, 1));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("65");
    }

    @Test
    void shouldCalculateCorrectTotal_whenInfantsAddNothingToTotal() {
        stubAdultPrice();
        stubInfantPrice();
        Booking bookingWithInfant = new Booking(1L,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.INFANT, 1));

        Booking bookingWithoutInfant = new Booking(1L,
                new TicketRequest(TicketType.ADULT, 1));

        assertThat(calculator.calculate(bookingWithInfant))
                .isEqualByComparingTo(calculator.calculate(bookingWithoutInfant));
    }

    @Test
    void shouldCalculateCorrectTotal_whenMaximumAdultTickets() {
        stubAdultPrice();
        Booking booking = new Booking(1L, new TicketRequest(TicketType.ADULT, 25));

        BigDecimal total = calculator.calculate(booking);

        assertThat(total).isEqualByComparingTo("625");
    }
}
