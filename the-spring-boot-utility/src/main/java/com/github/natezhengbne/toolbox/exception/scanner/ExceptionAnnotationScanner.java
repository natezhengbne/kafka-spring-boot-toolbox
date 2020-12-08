package com.github.natezhengbne.toolbox.exception.scanner;

import com.github.natezhengbne.toolbox.annotation.scanner.BaseAnnotationScanner;
import com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice;
import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Setter
public class ExceptionAnnotationScanner extends BaseAnnotationScanner {

    public List<Method> getExceptionHandlingMethods() {
        return super.getMappingAnnotatedMethods(ExceptionHandlerMapping.class, ExceptionAdvice.class);
    }
}

