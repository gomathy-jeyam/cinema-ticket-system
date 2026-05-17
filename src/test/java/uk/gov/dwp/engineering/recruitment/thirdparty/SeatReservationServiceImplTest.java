package uk.gov.dwp.engineering.recruitment.thirdparty;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(SeatReservationService.class)
class SeatReservationServiceImplTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    SeatReservationService seatReservationService;

    @Test
    void shouldReturnOk_whenReserveSeatsIsInvoked() {
        ResponseEntity<String> response = seatReservationService.reserveSeats(121L, 3L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
