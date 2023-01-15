package com.example.bugzordclient;

import feign.Retryer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(clients = BugzordFeignClient.class)
public class FeignConfig {
    private Integer connectTimeout = 1000;
    private Integer readTimeout = 1000;

    private String loggerLever = "basic";
}
