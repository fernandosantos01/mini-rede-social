package com.example.mini_rede_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MiniRedeSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniRedeSocialApplication.class, args);
	}

}
