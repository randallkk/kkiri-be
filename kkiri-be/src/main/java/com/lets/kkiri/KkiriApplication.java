package com.lets.kkiri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class KkiriApplication {

    public static void main(String[] args) {
        SpringApplication.run(KkiriApplication.class, args);
    }

}
