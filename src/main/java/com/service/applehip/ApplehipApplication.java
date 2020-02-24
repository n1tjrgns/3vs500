package com.service.applehip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Jpa Auditing을 활성화 시키기 위한 어노테이션
@SpringBootApplication
public class ApplehipApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApplehipApplication.class, args);
    }

}
