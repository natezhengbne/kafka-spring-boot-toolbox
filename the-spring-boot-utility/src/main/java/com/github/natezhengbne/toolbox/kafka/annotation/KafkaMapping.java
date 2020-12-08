package com.github.natezhengbne.toolbox.kafka.annotation;

import com.github.natezhengbne.toolbox.kafka.message.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface KafkaMapping {
    OperationType operationType();

    String operation();
}
