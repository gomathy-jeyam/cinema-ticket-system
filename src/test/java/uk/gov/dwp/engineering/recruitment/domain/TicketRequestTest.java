package uk.gov.dwp.engineering.recruitment.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.dwp.engineering.recruitment.domain.TicketType.ADULT;
import static uk.gov.dwp.engineering.recruitment.domain.TicketType.CHILD;
import static uk.gov.dwp.engineering.recruitment.domain.TicketType.INFANT;

class TicketRequestTest {

    @Test
    void shouldReturnTypeAndCount_whenInfantTicketRequestCreated() {
        TicketRequest ticketRequest = new TicketRequest(INFANT, 0);

        assertThat(ticketRequest.type()).isEqualTo(INFANT);
        assertThat(ticketRequest.ticketCount()).isZero();
    }

    @Test
    void shouldReturnTypeAndCount_whenChildTicketRequestCreated() {
        TicketRequest ticketRequest = new TicketRequest(CHILD, 1);

        assertThat(ticketRequest.type()).isEqualTo(CHILD);
        assertThat(ticketRequest.ticketCount()).isEqualTo(1);
    }

    @Test
    void shouldReturnTypeAndCount_whenAdultTicketRequestCreated() {
        TicketRequest ticketRequest = new TicketRequest(ADULT, 2);

        assertThat(ticketRequest.type()).isEqualTo(ADULT);
        assertThat(ticketRequest.ticketCount()).isEqualTo(2);
    }
}
