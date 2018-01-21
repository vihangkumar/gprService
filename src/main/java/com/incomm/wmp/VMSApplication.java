package com.incomm.wmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableAutoConfiguration
@EnableCaching
@SpringBootApplication
public class VMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(VMSApplication.class, args);
    }
}
