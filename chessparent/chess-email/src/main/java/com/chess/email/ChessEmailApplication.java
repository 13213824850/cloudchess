package com.chess.email;

import com.chess.email.matchplay.init.EndApplicationStart;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.chess")
public class ChessEmailApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChessEmailApplication.class);
	}

}
