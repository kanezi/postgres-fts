package hr.kanezi.postgres.fts;

import hr.kanezi.postgres.fts.quotes.QuotesViewRepository;
import lombok.Value;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FtsApplication {
	public static void main(String[] args) {
		SpringApplication.run(FtsApplication.class, args);
	}
}
