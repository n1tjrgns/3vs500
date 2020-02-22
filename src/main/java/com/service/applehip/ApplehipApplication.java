package com.service.applehip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ApplehipApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplehipApplication.class, args);
    }

}
