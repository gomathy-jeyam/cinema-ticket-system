package uk.gov.dwp.engineering.recruitment.booking.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.engineering.recruitment.booking.policy.SeatReservationPolicy;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandardSeatCalculatorTest {

    @Mock
    private SeatReservationPolicy seatReservationPolicy;

    private StandardSeatCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new StandardSeatCalculator(seatReservationPolicy);
    }

    private void stubAdultRequiresSeat() {
        when(seatReservationPolicy.requiresSeat(TicketType.ADULT)).thenReturn(true);
    }

    private void stubChildRequiresSeat() {
        when(seatReservationPolicy.requiresSeat(TicketType.CHILD)).thenReturn(true);
    }

    private void stubInfantNoSeat() {
        when(seatReservationPolicy.requiresSeat(TicketType.INFANT)).thenReturn(false);
    }

    @Test
    void shouldReturnSeatCount_whenSingleAdultTicket() {
        stubAdultRequiresSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.ADULT, 1));

        assertThat(calculator.calculate(booking)).isEqualTo(1);
    }

    @Test
    void shouldReturnSeatCount_whenMultipleAdultTickets() {
        stubAdultRequiresSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.ADULT, 5));

        assertThat(calculator.calculate(booking)).isEqualTo(5);
    }

    @Test
    void shouldReturnSeatCount_whenSingleChildTicket() {
        stubChildRequiresSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.CHILD, 1));

        assertThat(calculator.calculate(booking)).isEqualTo(1);
    }

    @Test
    void shouldReturnSeatCount_whenMultipleChildTickets() {
        stubChildRequiresSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.CHILD, 3));

        assertThat(calculator.calculate(booking)).isEqualTo(3);
    }

    @Test
    void shouldReturnZeroSeats_whenSingleInfantTicket() {
        stubInfantNoSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.INFANT, 1));

        assertThat(calculator.calculate(booking)).isEqualTo(0);
    }

    @Test
    void shouldReturnZeroSeats_whenMultipleInfantTickets() {
        stubInfantNoSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.INFANT, 5));

        assertThat(calculator.calculate(booking)).isEqualTo(0);
    }

    @Test
    void shouldNotCountInfantSeats_whenAdultAndInfantTickets() {
        stubAdultRequiresSeat();
        stubInfantNoSeat();
        Booking booking = new Booking(101L,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.INFANT, 2));

        assertThat(calculator.calculate(booking)).isEqualTo(2);
    }

    @Test
    void shouldCountAllNonInfantSeats_whenFullMixedBooking() {
        stubAdultRequiresSeat();
        stubChildRequiresSeat();
        stubInfantNoSeat();
        Booking booking = new Booking(101L,
                new TicketRequest(TicketType.ADULT, 3),
                new TicketRequest(TicketType.CHILD, 2),
                new TicketRequest(TicketType.INFANT, 2));

        assertThat(calculator.calculate(booking)).isEqualTo(5);
    }

    @Test
    void shouldReturnSeatCount_whenMaximumAdultTickets() {
        stubAdultRequiresSeat();
        Booking booking = new Booking(101L, new TicketRequest(TicketType.ADULT, 25));

        assertThat(calculator.calculate(booking)).isEqualTo(25);
    }

    @Test
    void shouldReturnAdultSeatCountOnly_whenInfantsDoNotIncreaseSeatCount() {
        stubAdultRequiresSeat();
        stubInfantNoSeat();
        Booking bookingWithInfant = new Booking(101L,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.INFANT, 1));

        Booking bookingWithoutInfant = new Booking(101L,
                new TicketRequest(TicketType.ADULT, 1));

        assertThat(calculator.calculate(bookingWithInfant))
                .isEqualTo(calculator.calculate(bookingWithoutInfant));
    }
}
