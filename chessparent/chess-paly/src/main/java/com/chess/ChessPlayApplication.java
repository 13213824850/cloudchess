package com.chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 2:47 2019/3/15 0015
 * @Modifide by:
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ChessPlayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChessPlayApplication.class);
    }
}
