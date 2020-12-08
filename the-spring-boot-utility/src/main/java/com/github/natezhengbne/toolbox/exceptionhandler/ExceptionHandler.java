package com.github.natezhengbne.toolbox.exceptionhandler;

import com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice;
import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;
import com.github.natezhengbne.toolbox.exceptions.ExceptionHandlerMethodNotFoundException;
import com.github.natezhengbne.toolbox.exceptions.KafkaException;
import org.apache.kafka.common.errors.SerializationException;

@ExceptionAdvice
public class ExceptionHandler {

    @ExceptionHandlerMapping(ExceptionHandlerMethodNotFoundException.class)
    public void processMethodNotFoundException (Throwable error) {
        error.printStackTrace();
    }

    @ExceptionHandlerMapping(KafkaException.class)
    public void processKafkaException (Throwable error) {
        error.printStackTrace();
    }

    @ExceptionHandlerMapping(SerializationException.class)
    public void processKafkaSerializationException (Throwable error) {
        error.printStackTrace();
    }


}
