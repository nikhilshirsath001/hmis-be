package com.suma.hmis_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HmisServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HmisServiceApplication.class, args);
	}

}
