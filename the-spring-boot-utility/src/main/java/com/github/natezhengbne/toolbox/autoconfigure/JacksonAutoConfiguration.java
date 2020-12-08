package com.github.natezhengbne.toolbox.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonAutoConfiguration {

    /**
     * Spring Boot web starter provides an auto configured ObjectMapper bean out of the box.
     * So ObjectMapper is only injected if spring web starter is not available.
     * @return customised Jackson ObjectMapper
     */
    @Bean
    @ConditionalOnMissingClass("org.springframework.http.converter.json.Jackson2ObjectMapperBuilder")
    ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

}
