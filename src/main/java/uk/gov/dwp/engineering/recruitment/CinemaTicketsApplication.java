package uk.gov.dwp.engineering.recruitment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import uk.gov.dwp.engineering.recruitment.booking.config.BookingProperties;

@SpringBootApplication
@EnableConfigurationProperties(BookingProperties.class)
public class CinemaTicketsApplication {
  public static void main(String[] args) {
    SpringApplication.run(CinemaTicketsApplication.class, args);
  }
}
