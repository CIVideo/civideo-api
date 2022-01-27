package com.jsss.civideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CivideoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CivideoApplication.class, args);
    }

}
