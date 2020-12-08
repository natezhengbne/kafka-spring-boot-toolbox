package com.github.natezhengbne.toolbox.kafka.dispatcher


import spock.lang.Specification

class ObjectParserTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.util.ObjectParser objectParser

    com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder encoder

    void setup() {
        encoder = Mock(com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder)
        objectParser = new com.github.natezhengbne.toolbox.kafka.util.ObjectParser(encoder)
    }

//    def "test parseMethodParameters should return corresponding object List"() {
//        given: "TestClass with annotated method parameters"
//        and: "A request operation from a KafkaMessage with 2 parameters"
//        Integer paramA = 100
//        String paramB = "Sample String"
//        KafkaMessage message = new KafkaMessage(
//                operation: new Operation(
//                        params: [
//                                "paramA": "jsonBase64EncodedString".getBytes(),
//                                "paramB": "jsonBase64EncodedInteger".getBytes()
//                        ]
//                )
//        )
//        and: "A method with correct matching parameters"
//        Method method = TestClass1.getMethod("testMethod", Integer, String)
//
//        when:
//        List<Object> objects = objectParser.parseMethodParameters(message, method)
//
//        then:
//        1 * encoder.decode(message.operation.params["paramA"], paramA.class) >> paramA
//        1 * encoder.decode(message.operation.params["paramB"], paramB.class) >> paramB
//        objects.size() == message.operation.params.size()
//        objects == [paramA, paramB]
//        0 * _
//    }
//
//    def "test parseMethodParameters should throw exception if parameter types doesn`t match"() {
//        given: "TestClass with annotated method parameters"
//        and: "A request operation from a KafkaMessage with 2 parameters"
//        KafkaMessage message = new KafkaMessage(
//                operation: new Operation(
//                        params: [
//                                "paramA": "jsonBase64EncodedString1".getBytes(),
//                                "paramB": "jsonBase64EncodedString2".getBytes()
//                        ]
//                )
//        )
//        and: "A method with correct matching parameters"
//        Method method = TestClass1.getMethod("testMethod", Integer, String)
//
//        when:
//        objectParser.parseMethodParameters(message, method)
//
//        then:
//        1 * encoder.decode(message.operation.params["paramA"], Integer.class) >> {throw new SerializationException()}
//        thrown(SerializationException)
//        0 * _
//    }
//
//    def "test parseMethodParameters should should not fail if operation has no parameters"() {
//        given: "Test method with no parameters"
//        and: "A request operation from a KafkaMessage with no parameters"
//        KafkaMessage message = new KafkaMessage(
//                operation: new Operation(
//                        params: null
//                )
//        )
//        and: "A method with no parameters"
//        Method method = TestClass1.getMethod("noParamsMethod")
//
//        when:
//        List<Object> objects = objectParser.parseMethodParameters(message, method)
//
//        then:
//        objects.size() == 0
//        noExceptionThrown()
//        0 * _
//    }
//
//    def "test parseMethodParameters should return corresponding object List for kafka responses"() {
//        given: "method from TestResponseClass with annotated KafkaResponse parameter"
//        and: "A successful response with a response object"
//        Integer paramA = 100
//        KafkaMessage message = new KafkaMessage(
//                response: new Response(
//                        data: "jsonBase64EncodedString".getBytes()
//                )
//        )
//        and: "A method with correct matching parameters"
//        Method method = TestResponseClass.getMethod("testResponseMethod", Integer)
//
//        when:
//        List<Object> objects = objectParser.parseMethodParameters(message, method)
//
//        then:
//        1 * encoder.decode(message.response.data, paramA.class) >> paramA
//
//        objects.size() == 1
//        objects == [paramA]
//        0 * _
//    }
//
//    def "test parseMethodParameters should inject the KafkaMessage object if there is an KafkaMessage as parameter"() {
//        given: "TestClass with annotated method parameters"
//        and: "A request operation from a KafkaMessage with 2 parameters"
//        Integer paramA = 100
//        String paramB = "Sample String"
//        KafkaMessage message = new KafkaMessage(
//                operation: new Operation(
//                        params: [
//                                "paramA": "jsonBase64EncodedString".getBytes()
//                        ]
//                )
//        )
//        and: "A method with correct matching parameters"
//        Method method = TestKafkaMessageInjectionClass.getMethod("testParamAndMessageMethod", Integer, KafkaMessage)
//
//        when:
//        List<Object> objects = objectParser.parseMethodParameters(message, method)
//
//        then:
//        1 * encoder.decode(message.operation.params["paramA"], paramA.class) >> paramA
//        objects == [paramA, message]
//        0 * _
//    }
//
//
//    class TestClass1 {
//        void testMethod(@KafkaParam("paramA")Integer a, @KafkaParam("paramB")String b) {}
//        void noParamsMethod(){}
//    }
//
//    class TestResponseClass {
//        void testResponseMethod(@KafkaResponse Integer a) {}
//        void noParamsMethod(){}
//    }
//
//    class TestKafkaMessageInjectionClass {
//        void testParamAndMessageMethod(@KafkaParam("paramA") Integer a, KafkaMessage message) {}
//    }
}
