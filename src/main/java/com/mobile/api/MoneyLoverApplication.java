package com.mobile.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableAspectJAutoProxy
@Slf4j
public class MoneyLoverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyLoverApplication.class, args);
	}
}
