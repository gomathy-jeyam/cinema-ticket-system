package uk.gov.dwp.engineering.recruitment.thirdparty;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.MockRestServiceServer;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RestClientTest(PaymentService.class)
class PaymentServiceImplTest {

    @Autowired
    MockRestServiceServer server;

    @Autowired
    private PaymentService paymentService;

    @Test
    void shouldReturnOk_whenDebitAccountIsInvoked() {
        ResponseEntity<String> response = paymentService.debitAccount(123L, BigDecimal.valueOf(1.00));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
