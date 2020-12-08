package com.github.natezhengbne.toolbox.exception.scanner;

import com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping;
import com.github.natezhengbne.toolbox.exceptions.ExceptionHandlerMethodNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExceptionMethodHandlerService {

    @Getter @Setter
    private Map<Class<?>, Method> operationsMapping;

    @Autowired
    private ExceptionAnnotationScanner scanner;

    @PostConstruct
    public void init() {
        operationsMapping = new HashMap<>();
        List<Method> methods = scanner.getExceptionHandlingMethods();
        for (Method method : methods) {
            ExceptionHandlerMapping myAnnotation = method.getAnnotation(ExceptionHandlerMapping.class);
            Class<?> value = myAnnotation.value();
            operationsMapping.put(value, method);
        }
    }

    public Method getHandlerMethod(Class<?> className) {
        Method method = operationsMapping.get(className);
        if (method == null) {
            String message = "The method for Exception class : "+ className + " was not found";
            throw new ExceptionHandlerMethodNotFoundException(message);
        }
        return operationsMapping.get(className);
    }
}
