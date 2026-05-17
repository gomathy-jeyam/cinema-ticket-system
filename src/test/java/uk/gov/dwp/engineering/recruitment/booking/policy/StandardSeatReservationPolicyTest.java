package uk.gov.dwp.engineering.recruitment.booking.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.dwp.engineering.recruitment.domain.TicketType;

import static org.assertj.core.api.Assertions.assertThat;

class StandardSeatReservationPolicyTest {

    private StandardSeatReservationPolicy policy;

    @BeforeEach
    void setUp() {
        policy = new StandardSeatReservationPolicy();
    }

    @Test
    void shouldRequireSeat_whenAdultTicketType() {
        assertThat(policy.requiresSeat(TicketType.ADULT)).isTrue();
    }

    @Test
    void shouldRequireSeat_whenChildTicketType() {
        assertThat(policy.requiresSeat(TicketType.CHILD)).isTrue();
    }

    @Test
    void shouldNotRequireSeat_whenInfantTicketType() {
        assertThat(policy.requiresSeat(TicketType.INFANT)).isFalse();
    }
}
