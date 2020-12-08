package com.github.natezhengbne.toolbox.kafka.bind

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class ObjectEncoderTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder encoder

    ObjectMapper objectMapper

    void setup() {
        objectMapper = new ObjectMapper()
        encoder = new com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder(objectMapper: objectMapper)
    }

    // commented because discard use object mapper convert object to byte array
//    def "test decode object"() {
//        given:
//        Operation originalObject = Operation.newBuilder()
//                .setType(OperationType.GET)
//                .setName("test")
//                .setParams(null).build()
//        byte[] encodedObject = Base64.encoder.encode(objectMapper.writeValueAsBytes(originalObject))
//
//        when:
//        Object decodedObject = encoder.decode(encodedObject, Operation.class)
//
//        then:
//        decodedObject == originalObject
//    }

    // commented because discard use object mapper convert object to byte array
//    def "test encode object"() {
//        given:
//        Operation objectToBeEncoded = Operation.newBuilder()
//                .setType(OperationType.GET)
//                .setName("test")
//                .setParams(null).build()
//
//        when:
//        byte[] encodedObject = encoder.encode(objectToBeEncoded)
//        byte [] decodedJson = Base64.decoder.decode(encodedObject)
//
//        then:
//        objectMapper.readValue(decodedJson, Operation.class) == objectToBeEncoded
//    }

//    def "test decode with wrong object type should throw Exception"() {
//        given:
//        String originalObject = "Some String"
//        byte[] encodedObject = Base64.encoder.encode(objectMapper.writeValueAsBytes(originalObject))
//
//        when:
//        encoder.decode(encodedObject, Integer.class)
//
//        then:
//        thrown(KafkaDeserializationException)
//    }

//    def "test encode map"() {
//        given:
//        Map<String,Object> params = [
//                "paramA" : "ABC",
//                "paramB" : 5,
//                "paramC" : Operation.newBuilder().setName("test").setType(OperationType.GET).setParams(null).build()
//        ]
////        new Operation(OperationType.GET, "test",  null)
//        and: "expectedResult"
//        Map<String, byte[]> expectedEncodedParams = [
//                "paramA" : encoder.encode(params["paramA"]),
//                "paramB" : encoder.encode(params["paramB"]),
//                "paramC" : encoder.encode(params["paramC"]),
//        ]
//
//        when:
//        Map<String, byte[]> encodedParams = encoder.encodeMap(params)
//
//        then:
//        encodedParams == expectedEncodedParams
//    }

//    def "test encode map with invalid params"() {
//        when:
//        Map<String, byte[]> encodedParams = encoder.encodeMap(params)
//
//        then:
//        encodedParams == expectedResponse
//
//        where:
//        params | expectedResponse
//        [:]    | [:]
//        null   | null
//    }

}
