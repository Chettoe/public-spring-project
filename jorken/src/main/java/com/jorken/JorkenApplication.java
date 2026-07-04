package com.jorken;

import com.jorken.domain.Betreuer.Betreuer;
import com.jorken.service.betreuer.BetreuerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class JorkenApplication {

	public static void main(String[] args) {
		SpringApplication.run(JorkenApplication.class, args);
	}

}
