package com.chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 1:51 2019/3/11 0011
 * @Modifide by:
 */

@EnableDiscoveryClient
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients(basePackages = "com.chess")
public class AuthCenterService {
    public static void main(String[] args) {
        SpringApplication.run(AuthCenterService.class);
    }
}
