package com.github.natezhengbne.toolbox.kafka.bind


import com.github.natezhengbne.toolbox.exceptions.ExceptionHandlerMethodNotFoundException
import spock.lang.Specification

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class ExceptionMethodHandlerServiceTest extends Specification{

    com.github.natezhengbne.toolbox.exception.scanner.ExceptionMethodHandlerService service

    Map<Class<?>, Method> operationsMapping

    com.github.natezhengbne.toolbox.exception.scanner.ExceptionAnnotationScanner scanner

    void setup() {
        scanner = Mock(com.github.natezhengbne.toolbox.exception.scanner.ExceptionAnnotationScanner)
        service = new com.github.natezhengbne.toolbox.exception.scanner.ExceptionMethodHandlerService (operationsMapping: operationsMapping, scanner: scanner)
    }

    def "test scanHandlerMappings populates the operations map"() {
        given:
        List<Method> methods = [Foo.class.getMethod("processNullPointerException", Throwable), Foo.class.getMethod("processInvocationTargetException", Throwable)]

        when:
        service.init()

        then:
        1 * scanner.getExceptionHandlingMethods() >> methods
        service.operationsMapping.size() == 2
        service.operationsMapping[NullPointerException] == Foo.getMethod("processNullPointerException", Throwable)
        service.operationsMapping[InvocationTargetException] == Foo.getMethod("processInvocationTargetException", Throwable)
    }

    def "test getHandlerMethod returns correct handler for operation"() {
        given:
        operationsMapping = [
                (NullPointerException.class) : Foo.getMethod("processNullPointerException", Throwable),
                (InvocationTargetException.class) : Foo.getMethod("processInvocationTargetException", Throwable)
        ]
        service.setOperationsMapping(operationsMapping)

        when:
        Method selectedMethod = service.getHandlerMethod(NullPointerException.class)

        then:
        selectedMethod ==  operationsMapping.get(NullPointerException)
    }

    def "test scanHandlerMappings populates the operations map more than 1 method is defined for the same exception class type"() {
        given:
        List<Method> methods = [Foo1.class.getMethod("processNullPointerException", Throwable),
                                Foo1.class.getMethod("processInvocationTargetException", Throwable),
                                Foo1.class.getMethod("processNullPointerException1", Throwable)]

        when:
        service.init()

        then:
        1 * scanner.getExceptionHandlingMethods() >> methods
        service.operationsMapping.size() == 2
        service.operationsMapping[NullPointerException] == Foo1.getMethod("processNullPointerException1", Throwable)
        service.operationsMapping[InvocationTargetException] == Foo1.getMethod("processInvocationTargetException", Throwable)
    }

    def "test getHandlerMethod should throw exception if no handler is found"() {
        given:
        operationsMapping = [
                (NullPointerException.class) : Foo.getMethod("processNullPointerException", Throwable),
                (InvocationTargetException.class) : Foo.getMethod("processInvocationTargetException", Throwable)
        ]
        service.setOperationsMapping(operationsMapping)

        when:
        service.getHandlerMethod(ArrayIndexOutOfBoundsException)

        then:
        thrown(ExceptionHandlerMethodNotFoundException)
    }

    @com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice
    private class Foo {

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(NullPointerException.class)
        public String processNullPointerException (Throwable error) {
           return new String ("Processed in System Exception : "+error.getMessage())
        }

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(InvocationTargetException.class)
        public String processInvocationTargetException (Throwable error) {
           return new String ("Processed in Invocation Target Exception : "+error.getMessage())
        }
    }

    @com.github.natezhengbne.toolbox.exception.annotation.ExceptionAdvice
    private class Foo1 {

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(NullPointerException.class)
        public String processNullPointerException (Throwable error) {
            return new String ("Processed in System Exception : "+error.getMessage())
        }

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(InvocationTargetException.class)
        public String processInvocationTargetException (Throwable error) {
            return new String ("Processed in Invocation Target Exception : "+error.getMessage())
        }

        @com.github.natezhengbne.toolbox.exception.annotation.ExceptionHandlerMapping(NullPointerException.class)
        public String processNullPointerException1 (Throwable error) {
            return new String ("Processed in System Exception : "+error.getMessage())
        }
    }
}
