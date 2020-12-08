package com.github.natezhengbne.toolbox.kafka.bind


import spock.lang.Specification

import java.lang.reflect.Method

class ExceptionAnnotationScannerTest extends Specification{

    com.github.natezhengbne.toolbox.exception.scanner.ExceptionAnnotationScanner scanner

    void setup() {
        scanner = new com.github.natezhengbne.toolbox.exception.scanner.ExceptionAnnotationScanner()
        scanner.setBasePackage("scanner.test.exception")
    }

    def "test getExceptionMappingAnnotatedMethods should scan for ExceptionMapping methods"() {
        given: "Classes on package scanner.test.exception"

        when:
        List<Method> foundMethods = scanner.getExceptionHandlingMethods()

        then:
        println(foundMethods)
        foundMethods.size() == 4
    }
}
