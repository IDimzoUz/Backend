package uz.idimzo.beck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BeckApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeckApplication.class, args);
	}

}
