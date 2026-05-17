package uk.gov.dwp.engineering.recruitment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uk.gov.dwp.engineering.recruitment.booking.calculator.PriceCalculator;
import uk.gov.dwp.engineering.recruitment.booking.calculator.SeatCalculator;
import uk.gov.dwp.engineering.recruitment.booking.validation.ValidationRule;
import uk.gov.dwp.engineering.recruitment.domain.Booking;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.exception.InvalidBookingException;
import uk.gov.dwp.engineering.recruitment.exception.validation.PurchaseValidationException;
import uk.gov.dwp.engineering.recruitment.booking.presentation.BookingReceiptFormatter;
import uk.gov.dwp.engineering.recruitment.thirdparty.PaymentService;
import uk.gov.dwp.engineering.recruitment.thirdparty.SeatReservationService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CinemaTicketsServiceImpl implements CinemaTicketsService {
    private static final Logger log = LoggerFactory.getLogger(CinemaTicketsServiceImpl.class);
    private final PaymentService paymentService;
    private final SeatReservationService seatReservationService;
    private final List<ValidationRule> validationRules;
    private final SeatCalculator seatCalculator;
    private final PriceCalculator priceCalculator;
    private final BookingReceiptFormatter bookingReceiptFormatter;

    public CinemaTicketsServiceImpl(PaymentService paymentService,
                SeatReservationService seatReservationService,
                List<ValidationRule> validationRules,
                SeatCalculator seatCalculator,
                PriceCalculator ticketPriceCalculator,
                BookingReceiptFormatter bookingReceiptFormatter) {
        this.paymentService = paymentService;
        this.seatReservationService = seatReservationService;
        this.validationRules = validationRules;
        this.seatCalculator = seatCalculator;
        this.priceCalculator = ticketPriceCalculator;
        this.bookingReceiptFormatter = bookingReceiptFormatter;
    }

    @Override
    public String purchaseTickets(final Long accountId, final TicketRequest... ticketRequests) throws InvalidBookingException {
          try{
              log.info("Processing purchase for account {}", accountId);
              Booking booking = new Booking(accountId, ticketRequests);
              validationRules.forEach(validationRule -> validationRule.validate(booking));

              BigDecimal totalPrice = priceCalculator.calculate(booking);
              long seatsCount = seatCalculator.calculate(booking);

              log.info("account: {} - Seat to be reserved: {} and amount to be paid: {}", accountId, seatsCount, totalPrice);

              //Assumption: third party services work correctly with no defects
              seatReservationService.reserveSeats(accountId,seatsCount);
              paymentService.debitAccount(accountId, totalPrice);

              log.info("Payment and seat reservation is completed for account {} ", accountId);

              return bookingReceiptFormatter.build(booking, seatsCount, totalPrice);
          }
          catch (PurchaseValidationException ex){
              log.error("Error in processing purchase request for account {} - {} ", accountId, ex.getMessage());
              throw new InvalidBookingException(ex.getMessage());
          }
    }

}
