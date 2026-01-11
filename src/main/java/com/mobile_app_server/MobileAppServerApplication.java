package com.mobile_app_server;

import org.activiti.engine.RuntimeService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MobileAppServerApplication implements CommandLineRunner {

    private final RuntimeService runtimeService;

    public MobileAppServerApplication(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MobileAppServerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println(">>> Starting BPMN process...");
        runtimeService.startProcessInstanceByKey("simpleProcess");
        System.out.println(">>> Process finished!");
    }

}
