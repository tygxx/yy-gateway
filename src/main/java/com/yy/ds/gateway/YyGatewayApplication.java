package com.yy.ds.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
// scanBasePackages指定扫描包，logback-spring.xml配置才会生效
@SpringBootApplication(scanBasePackages = "com.yy.ds")
public class YyGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(YyGatewayApplication.class, args);
    }

}
