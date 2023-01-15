package com.example.krzysztofclient;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = KrzychuFeignClient.class)
public class KrzysztofFeignConfig {
    private Integer connectTimeout = 1000000;
    private Integer readTimeout = 1000000;

    private String loggerLever = "basic";
}
