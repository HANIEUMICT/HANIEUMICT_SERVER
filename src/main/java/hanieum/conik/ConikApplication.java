package hanieum.conik;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableRetry
@EnableAsync
@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
public class ConikApplication {
	public static void main(String[] args) {
		SpringApplication.run(ConikApplication.class, args);
	}

}
