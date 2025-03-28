package com.mobile_app_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MobileAppServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MobileAppServerApplication.class, args);
    }

}
