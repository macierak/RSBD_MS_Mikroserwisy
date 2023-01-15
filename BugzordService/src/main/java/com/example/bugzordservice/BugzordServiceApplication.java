package com.example.bugzordservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class BugzordServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BugzordServiceApplication.class, args);
    }

}
