package com.derso.reservas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
		"com.derso.reservas.*",
		"com.derso.controlesessao.*"
})
@EntityScan(basePackages = {
		"com.derso.reservas.*",
		"com.derso.controlesessao.*"
})
@ComponentScan(basePackages = {
		"com.derso.reservas.*",
		"com.derso.controlesessao.*"
})
public class ReservasApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservasApplication.class, args);
	}

}
