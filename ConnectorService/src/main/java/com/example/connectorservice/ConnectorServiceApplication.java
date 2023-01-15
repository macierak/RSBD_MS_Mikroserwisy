package com.example.connectorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients//(basePackages = {"com.example.bugzordclient.BugzordFeignClient"})
@SpringBootApplication(scanBasePackages = {"com.example.bugzordclient", "com.example.connectorservice", "com.example.krzysztofclient"})
public class ConnectorServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConnectorServiceApplication.class, args);
    }

}
