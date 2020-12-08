package com.github.natezhengbne.appsupport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringKafkaIntegrationTestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringKafkaIntegrationTestsApplication.class, args);
    }
}