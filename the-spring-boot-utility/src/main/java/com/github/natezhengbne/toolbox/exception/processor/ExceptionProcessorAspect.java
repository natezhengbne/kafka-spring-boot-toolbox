package com.github.natezhengbne.toolbox.exception.processor;

import com.github.natezhengbne.toolbox.exception.scanner.ExceptionMethodHandlerService;
import com.github.natezhengbne.toolbox.exceptions.KafkaException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Aspect
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionProcessorAspect {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ExceptionMethodHandlerService methodHandlerService;

    @Pointcut("execution(public * com.github.natezhengbne..*(..))")
    public void getExceptionPointcut(){}

    @Pointcut("execution(public * com.github.natezhengbne.toolbox.kafka.dispatcher.KafkaToKafkaDispatcher.dispatch(..))")
    public void getDispatchMethodPointcut(){}

    @Pointcut("@annotation(com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping)")
    public void getKafkaMappingAnnotatedMethodPointcut(){}

    @Pointcut("@annotation(org.springframework.kafka.annotation.KafkaListener)")
    public void getKafkaListenerAnnotatedPointcut(){}

    @AfterThrowing(pointcut = "getExceptionPointcut()&& !getKafkaMappingAnnotatedMethodPointcut() " +
            "&& !getKafkaListenerAnnotatedPointcut() && !getDispatchMethodPointcut()", throwing = "error")
    public Object processor(JoinPoint call, Throwable error) {
        if(null != call && !(error instanceof KafkaException)) {
            try {
                Method method = methodHandlerService.getHandlerMethod(error.getClass());
                List<Object> parametersList = new ArrayList<Object>() {{add(error);}};
                Object result = method.invoke(applicationContext.getBean(method.getDeclaringClass()), parametersList.toArray());
                return result;
            } catch (IllegalAccessException | InvocationTargetException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
