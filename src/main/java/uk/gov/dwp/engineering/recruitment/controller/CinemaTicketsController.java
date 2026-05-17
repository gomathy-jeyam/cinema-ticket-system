package uk.gov.dwp.engineering.recruitment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.dwp.engineering.recruitment.domain.TicketRequest;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;
import uk.gov.dwp.engineering.recruitment.service.CinemaTicketsService;

import java.util.List;

@RestController
@RequestMapping("/cinema")
public class CinemaTicketsController {

  /**
   * The Ticket Booking Service.
   */
  private final CinemaTicketsService cinemaTicketsService;

  public CinemaTicketsController(final CinemaTicketsService cinemaTicketsService) {
    this.cinemaTicketsService = cinemaTicketsService;
  }
}


