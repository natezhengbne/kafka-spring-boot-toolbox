package com.github.natezhengbne.toolbox.exception.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfiguration {
    @Bean
    public ExceptionProcessorAspect notifyAspect() {return new ExceptionProcessorAspect();}
}
