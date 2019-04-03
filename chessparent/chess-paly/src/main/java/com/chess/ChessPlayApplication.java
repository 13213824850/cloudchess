package com.chess;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:47 2019/3/15 0015
 * @Modifide by:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableRabbit
@EnableFeignClients(basePackages = "com.chess")
public class ChessPlayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChessPlayApplication.class);
    }
}
