package uk.gov.dwp.engineering.recruitment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CinemaTicketsApplicationTest {

    @Autowired
    ApplicationContext context;

    @Test
    void shouldLoadApplicationContext_whenSpringBootStarts() {
        assertThat(context).isNotNull();
    }

    @Test
    void shouldStartWithoutException_whenMainMethodInvoked() {
        CinemaTicketsApplication.main(new String[]{"--spring.main.web-environment=false"});
    }
}
