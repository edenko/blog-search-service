package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class ExternalApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExternalApplication.class, args);
    }

}
