package com.github.natezhengbne.toolbox.kafka.bind


import spock.lang.Specification

import java.lang.reflect.Method

class KafkaAnnotationScannerTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.scanner.KafkaAnnotationScanner scanner

    void setup() {
        scanner = new com.github.natezhengbne.toolbox.kafka.scanner.KafkaAnnotationScanner()
        scanner.setBasePackage("scanner.test")
    }

    def "test getKafkaMappingAnnotatedMethods should scan for KafkaMapping methods"() {
        given: "Classes on package scanner.test"

        when:
        List<Method> foundMethods = scanner.getKafkaMappingAnnotatedMethods()

        then:
        foundMethods.size() == 3
    }

}
