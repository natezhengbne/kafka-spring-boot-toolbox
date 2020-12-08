package com.github.natezhengbne.appsupport;

import com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice;
import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.SerializationException;

@Slf4j
@ExceptionAdvice
public class ExceptionHandler {

    @ExceptionHandlerMapping(SerializationException.class)
    public void basicExceptionHandler5(Exception ex){
        log.error(ex.getMessage(),ex);
    }

}
