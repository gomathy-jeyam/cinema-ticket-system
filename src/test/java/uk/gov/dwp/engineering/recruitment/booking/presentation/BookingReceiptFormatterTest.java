package uk.gov.dwp.engineering.recruitment.booking.presentation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class BookingReceiptFormatterTest {

    private BookingReceiptFormatter bookingReceiptFormatter;

    @BeforeEach
    void setUp(){
        bookingReceiptFormatter = new BookingReceiptFormatter();
    }

    @Test
    void shouldContainAccountId_whenBookingIsFormatted() {
        Booking booking = new Booking(42L, new TicketRequest(TicketType.ADULT, 1));

        String result = bookingReceiptFormatter.build(booking, 1, BigDecimal.valueOf(25));

        assertThat(result).contains("Account: 42");
    }

    @Test
    void shouldContainTicketLine_whenSingleTicketType() {
        Booking booking = new Booking(123L, new TicketRequest(TicketType.ADULT, 2));

        String result = bookingReceiptFormatter.build(booking, 2, BigDecimal.valueOf(50));

        assertThat(result).contains("2 x ADULT");
    }

    @Test
    void shouldContainAllTicketTypesInLine_whenMixedBooking() {
        Booking booking = new Booking(121L,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.CHILD, 1),
                new TicketRequest(TicketType.INFANT, 1));

        String result = bookingReceiptFormatter.build(booking, 3, BigDecimal.valueOf(65));

        assertThat(result)
                .contains("2 x ADULT")
                .contains("1 x CHILD")
                .contains("1 x INFANT");
    }

    @Test
    void shouldContainSeatCount_whenFormattedOutput() {
        Booking booking = new Booking(101L,
                new TicketRequest(TicketType.ADULT, 2),
                new TicketRequest(TicketType.INFANT, 1));

        String result = bookingReceiptFormatter.build(booking, 2, BigDecimal.valueOf(50));

        assertThat(result).contains("Seats reserved: 2");
    }

    @Test
    void shouldContainZeroSeats_whenOnlyInfantTickets() {
        Booking booking = new Booking(101L, new TicketRequest(TicketType.INFANT, 2));

        String result = bookingReceiptFormatter.build(booking, 0, BigDecimal.ZERO);

        assertThat(result).contains("Seats reserved: 0");
    }

    @Test
    void shouldFormatAmountToTwoDecimalPlaces_whenWholeNumberPrice() {
        Booking booking = new Booking(101L, new TicketRequest(TicketType.ADULT, 1));

        String result = bookingReceiptFormatter.build(booking, 1, BigDecimal.valueOf(25));

        assertThat(result).contains("£25.00");
    }

    @Test
    void shouldContainConfirmationHeader_whenAnyValidBooking() {
        Booking booking = new Booking(101L, new TicketRequest(TicketType.ADULT, 1));

        String result = bookingReceiptFormatter.build(booking, 1, BigDecimal.valueOf(25));

        assertThat(result).contains("Booking confirmed");
    }

    @Test
    void shouldContainAllExpectedFields_whenCompleteBooking() {
        Booking booking = new Booking(121L,
                new TicketRequest(TicketType.ADULT, 1),
                new TicketRequest(TicketType.CHILD, 1));

        String result = bookingReceiptFormatter.build(booking, 2, new BigDecimal("40"));

        assertThat(result)
                .contains("Booking confirmed")
                .contains("Account: 121")
                .contains("1 x ADULT")
                .contains("1 x CHILD")
                .contains("Seats reserved: 2")
                .contains("£40.00");
    }
}
