package it.unical.fleetgo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class FleetGoApplication {
	public static void main(String[] args) {
		SpringApplication.run(FleetGoApplication.class, args);
	}
}