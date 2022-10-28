package com.example.jpaselfstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaSelfStudyApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(JpaSelfStudyApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
