package com.example.Formation.certificat.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;


@SpringBootApplication
@EnableFeignClients
@EnableRabbit
public class FormationCertificatServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FormationCertificatServiceApplication.class, args);
	}

}
