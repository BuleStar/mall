package com.mall.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hongfei
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MallGatewayNacosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MallGatewayNacosApplication.class, args);
    }

}
