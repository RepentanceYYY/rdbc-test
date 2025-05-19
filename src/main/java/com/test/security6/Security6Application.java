package com.test.security6;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.test.security6.mapper")
public class Security6Application {

    public static void main(String[] args) {
        SpringApplication.run(Security6Application.class, args);
        System.out.println("localhost:8080/swagger-ui.html");
    }
}
