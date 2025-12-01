package com.evgeniy.spring.springapplication;

import org.springframework.boot.SpringApplication;

public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestMailConfig.class).run(args);
    }

}
