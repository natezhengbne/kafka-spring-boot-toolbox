package com.github.natezhengbne.toolbox.kafka.dispatcher


import org.aspectj.lang.JoinPoint
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class ExceptionProcessorAspectTest extends Specification {

    com.github.natezhengbne.toolbox.exception.processor.ExceptionProcessorAspect aspect

    ApplicationContext context

    com.github.natezhengbne.toolbox.exception.scanner.ExceptionMethodHandlerService service

    JoinPoint call

    void setup() {
        context = Mock(ApplicationContext)
        service = Mock(com.github.natezhengbne.toolbox.exception.scanner.ExceptionMethodHandlerService)
        call = Mock(JoinPoint)
        aspect = new com.github.natezhengbne.toolbox.exception.processor.ExceptionProcessorAspect(context, service)
    }

    def "test dispatcher should invoke handler method and return result in message"() {
        given:
        Foo fooBean = new Foo()
        Method handlerMethod = Foo.getMethod("processNullPointerException", Throwable)
        Throwable error = Mock(Throwable)

        when:
        String message = (String) aspect.processor(call, error)

        then:
        1 * service.getHandlerMethod(error.getClass()) >> handlerMethod
        1 * context.getBean(Foo) >> fooBean
        message.contains("Processed in System Exception :")
    }

    @com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice
    class Foo {

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(NullPointerException.class)
        public String processNullPointerException (Throwable error) {
            return new String ("Processed in System Exception : "+error.getMessage())
        }

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(InvocationTargetException.class)
        public String processInvocationTargetException (Throwable error) {
            return new String ("Processed in Invocation Target Exception : "+error.getMessage())
        }
    }
}
