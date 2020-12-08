package com.github.natezhengbne.toolbox.kafka.bind


import com.github.natezhengbne.toolbox.exceptions.KafkaMethodNotFoundException
import com.github.natezhengbne.toolbox.kafka.message.OperationType
import spock.lang.Specification

import java.lang.reflect.Method

class KafkaMethodHandlerServiceTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.scanner.KafkaMethodHandlerService service

    Map <com.github.natezhengbne.toolbox.kafka.scanner.OperationKey,Method> operationsMapping
    com.github.natezhengbne.toolbox.kafka.scanner.KafkaAnnotationScanner scanner

    void setup() {
        operationsMapping = [
                (new com.github.natezhengbne.toolbox.kafka.scanner.OperationKey(OperationType.GET, "op1")): Foo.getMethod("methodA"),
                (new com.github.natezhengbne.toolbox.kafka.scanner.OperationKey(OperationType.GET, "op2")): Foo.getMethod("methodB")
        ]
        scanner = Mock(com.github.natezhengbne.toolbox.kafka.scanner.KafkaAnnotationScanner)
        service = new com.github.natezhengbne.toolbox.kafka.scanner.KafkaMethodHandlerService(operationsMapping: operationsMapping, scanner: scanner)
    }

    def "test getHandlerMethod returns correct handler for operation"() {
        given:
        OperationType operationType =  OperationType.GET
        String operation = "op1"

        when:
        Method selectedMethod = service.getHandlerMethod(operationType, operation)

        then:
        selectedMethod ==  operationsMapping[new com.github.natezhengbne.toolbox.kafka.scanner.OperationKey(operationType, operation)]
    }

    def "test scanHandlerMappings populates the operations map"() {
        given:
        List<Method> methods = [Foo.class.getMethod("methodA"), Foo.class.getMethod("methodB")]

        when:
        service.scanHandlerMappings()

        then:
        1 * scanner.getKafkaMappingAnnotatedMethods() >> methods
        service.operationsMapping.size() == 2
        service.operationsMapping[new com.github.natezhengbne.toolbox.kafka.scanner.OperationKey(OperationType.GET, "op1")] == Foo.getMethod("methodA")
        service.operationsMapping[new com.github.natezhengbne.toolbox.kafka.scanner.OperationKey(OperationType.GET, "op2")] == Foo.getMethod("methodB")
    }

    def "test getHandlerMethod should throw exception if not handler is found"() {
        given:
        OperationType operationType =  OperationType.GET
        String operation = "blah"

        when:
        service.getHandlerMethod(operationType, operation)

        then:
        thrown(KafkaMethodNotFoundException)
    }

    private class Foo {

        @com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping(operationType = OperationType.GET, operation="op1")
        String methodA () { return "a" }

        @com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping(operationType = OperationType.GET, operation="op2")
        String methodB () { return "b" }
    }

}

