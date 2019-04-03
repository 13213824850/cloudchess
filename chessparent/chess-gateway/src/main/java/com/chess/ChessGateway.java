package com.chess;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringCloudApplication
@EnableDiscoveryClient
public class ChessGateway {
public static void main(String[] args) {
	SpringApplication.run(ChessGateway.class);
}
}
