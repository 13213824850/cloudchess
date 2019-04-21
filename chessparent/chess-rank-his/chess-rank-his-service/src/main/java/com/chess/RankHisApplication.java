package com.chess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Auther: huang yuan li
 * @Description:
 * @date: Create in 下午 4:10 2019/3/30 0030
 * @Modifide by:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(value = "com.chess.rankhis.mapper")
@EnableFeignClients(basePackages = "com.chess")
public class RankHisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RankHisApplication.class);
    }
}
