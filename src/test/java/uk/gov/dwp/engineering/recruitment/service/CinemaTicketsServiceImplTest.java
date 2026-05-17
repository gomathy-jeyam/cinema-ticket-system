package uk.gov.dwp.engineering.recruitment.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.dwp.engineering.recruitment.booking.calculator.PriceCalculator;
import uk.gov.dwp.engineering.recruitment.booking.calculator.SeatCalculator;
import uk.gov.dwp.engineering.recruitment.booking.presentation.BookingReceiptFormatter;
import uk.gov.dwp.engineering.recruitment.booking.validation.ValidationRule;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.exception.InvalidBookingException;
import uk.gov.dwp.engineering.recruitment.exception.validation.InsufficientAdultTicketsException;
import uk.gov.dwp.engineering.recruitment.exception.validation.InvalidAccountException;
import uk.gov.dwp.engineering.recruitment.exception.validation.TicketLimitExceededException;
import uk.gov.dwp.engineering.recruitment.thirdparty.PaymentService;
import uk.gov.dwp.engineering.recruitment.thirdparty.SeatReservationService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CinemaTicketsServiceImplTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private SeatReservationService seatReservationService;

    @Mock
    private ValidationRule validationRule;

    @Mock
    private PriceCalculator priceCalculator;

    @Mock
    private SeatCalculator seatCalculator;

    @Mock
    private BookingReceiptFormatter bookingReceiptFormatter;

    private CinemaTicketsServiceImpl service;

    private static final Long ACCOUNT_ID = 101L;
    private static final TicketRequest ONE_ADULT  = new TicketRequest(TicketType.ADULT, 1);
    private static final TicketRequest TWO_ADULTS = new TicketRequest(TicketType.ADULT, 2);
    private static final TicketRequest ONE_CHILD  = new TicketRequest(TicketType.CHILD, 1);
    private static final TicketRequest ONE_INFANT = new TicketRequest(TicketType.INFANT, 1);

    @BeforeEach
    void setUp() {
        service = new CinemaTicketsServiceImpl(
                paymentService,
                seatReservationService,
                List.of(validationRule),
                seatCalculator,
                priceCalculator,
                bookingReceiptFormatter);

    }

    @Test
    void shouldReturnWhateverTheFormatterProduces_whenPurchaseSucceeds() {
        when(bookingReceiptFormatter.build(any(), anyLong(), any())).thenReturn("receipt text");
        stubCalculators(BigDecimal.valueOf(25), 1);

        String result = service.purchaseTickets(ACCOUNT_ID, ONE_ADULT);

        assertThat(result).isEqualTo("receipt text");
    }

    @Test
    void shouldPassCalculatedPriceAndSeatCountToFormatter_whenPurchaseSucceeds() {
        BigDecimal price = BigDecimal.valueOf(40);
        stubCalculators(price, 2);

        service.purchaseTickets(ACCOUNT_ID, TWO_ADULTS);

        verify(bookingReceiptFormatter).build(
                any(Booking.class),
                eq(2L),
                eq(price)
        );
    }

    @Test
    void shouldReserveSeatsBeforeChargingPayment_whenValidBookingRequestMade() {
        stubCalculators(BigDecimal.valueOf(25), 1);

        service.purchaseTickets(ACCOUNT_ID, ONE_ADULT);

        InOrder order = inOrder(seatReservationService, paymentService);
        order.verify(seatReservationService).reserveSeats(anyLong(), anyLong());
        order.verify(paymentService).debitAccount(anyLong(), any(BigDecimal.class));
    }

    @Test
    void shouldDebitTheCalculatedAmount_whenValidSingleAdultTicketRequestMade() {
        BigDecimal expectedTotal = BigDecimal.valueOf(75);
        stubCalculators(expectedTotal, 3);

        service.purchaseTickets(ACCOUNT_ID, new TicketRequest(TicketType.ADULT, 3));

        verify(paymentService).debitAccount(eq(ACCOUNT_ID), eq(expectedTotal));
    }

    @Test
    void shouldReserveCalculatedSeatCount_whenValidTwoAdultTicketsMade() {
        stubCalculators(BigDecimal.valueOf(50), 2);

        service.purchaseTickets(ACCOUNT_ID, TWO_ADULTS);

        verify(seatReservationService).reserveSeats(eq(ACCOUNT_ID), eq(2L));
    }

    @Test
    void shouldPassAccountIdToReservationService_whenValidRequestWithOneAdultMade() {
        long specificAccount = 42L;
        stubCalculators(BigDecimal.valueOf(25), 1);

        service.purchaseTickets(specificAccount, ONE_ADULT);

        verify(seatReservationService).reserveSeats(eq(specificAccount), anyLong());
    }

    @Test
    void shouldPassAccountIdToPaymentService_whenValidRequestWithOneAdultMade() {
        long specificAccount = 99L;
        stubCalculators(BigDecimal.valueOf(25), 1);

        service.purchaseTickets(specificAccount, ONE_ADULT);

        verify(paymentService).debitAccount(eq(specificAccount), any(BigDecimal.class));
    }

    @Test
    void shouldInvokeEachValidationRule_whenPurchaseIsProcessed() {
        stubCalculators(BigDecimal.valueOf(25), 1L);

        service.purchaseTickets(ACCOUNT_ID, ONE_ADULT);

        verify(validationRule).validate(any(Booking.class));
    }

    @Test
    void shouldRunAllRulesInRegisteredOrder_whenPurchaseRequestProcesses() {
        ValidationRule firstRule  = mock(ValidationRule.class);
        ValidationRule secondRule = mock(ValidationRule.class);

        CinemaTicketsServiceImpl multiRuleService = new CinemaTicketsServiceImpl(
                paymentService, seatReservationService,
                List.of(firstRule, secondRule), seatCalculator, priceCalculator, bookingReceiptFormatter);

        stubCalculators(BigDecimal.valueOf(25), 1L);
        multiRuleService.purchaseTickets(ACCOUNT_ID, ONE_ADULT);

        InOrder order = inOrder(firstRule, secondRule);
        order.verify(firstRule).validate(any(Booking.class));
        order.verify(secondRule).validate(any(Booking.class));
    }

    @Test
    void shouldThrowInvalidBookingException_whenAccountIsInvalid() {
        doThrow(new InvalidAccountException("Account id should be greater than 0"))
                .when(validationRule).validate(any(Booking.class));

        assertThatThrownBy(() -> service.purchaseTickets(ACCOUNT_ID, ONE_ADULT))
                .isInstanceOf(InvalidBookingException.class)
                .hasMessage("Account id should be greater than 0");
    }

    @Test
    void shouldThrowInvalidBookingException_whenTicketLimitIsExceeded() {
        doThrow(new TicketLimitExceededException("Cannot purchase more than 25 tickets per transaction"))
                .when(validationRule).validate(any(Booking.class));

        assertThatThrownBy(() -> service.purchaseTickets(ACCOUNT_ID, ONE_ADULT))
                .isInstanceOf(InvalidBookingException.class)
                .hasMessage("Cannot purchase more than 25 tickets per transaction");
    }

    @Test
    void shouldThrowInvalidBookingException_whenNoAdultTicketsPresent() {
        doThrow(new InsufficientAdultTicketsException("Child and Infant tickets cannot be purchased without at least one Adult ticket"))
                .when(validationRule).validate(any(Booking.class));

        assertThatThrownBy(() -> service.purchaseTickets(ACCOUNT_ID, ONE_CHILD))
                .isInstanceOf(InvalidBookingException.class);
    }

    @Test
    void shouldNotCallExternalServices_whenValidationFails() {
        doThrow(new InvalidAccountException("Account id should be greater than 0"))
                .when(validationRule).validate(any(Booking.class));

        assertThatThrownBy(() -> service.purchaseTickets(ACCOUNT_ID, ONE_ADULT))
                .isInstanceOf(InvalidBookingException.class);

        verifyNoInteractions(paymentService, seatReservationService);
    }

    @Test
    void shouldNotRunCalculators_whenValidationFails() {
        doThrow(new InvalidAccountException("Account id should be greater than 0"))
                .when(validationRule).validate(any(Booking.class));

        assertThatThrownBy(() -> service.purchaseTickets(ACCOUNT_ID, ONE_ADULT))
                .isInstanceOf(InvalidBookingException.class);

        verifyNoInteractions(priceCalculator, seatCalculator);
    }

    private void stubCalculators(BigDecimal price, long seats) {
        when(priceCalculator.calculate(any(Booking.class))).thenReturn(price);
        when(seatCalculator.calculate(any(Booking.class))).thenReturn(seats);
    }
}
