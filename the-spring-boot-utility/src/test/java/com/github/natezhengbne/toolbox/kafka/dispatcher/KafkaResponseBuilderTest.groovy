package com.github.natezhengbne.toolbox.kafka.dispatcher

import com.github.natezhengbne.toolbox.kafka.message.Header
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage
import com.github.natezhengbne.toolbox.kafka.message.Operation
import com.github.natezhengbne.toolbox.kafka.message.OperationType
import com.github.natezhengbne.toolbox.kafka.message.ResponseStatus
import spock.lang.Specification

class KafkaResponseBuilderTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder kafkaResponseBuilder

    com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder encoder

    void setup() {
        encoder = Mock(com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder)
        kafkaResponseBuilder = new com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder(encoder: encoder)
    }

    def "test BuildSuccessResponse should generate SUCCESS message and encode response object"() {
        given:
        KafkaMessage request = KafkaMessage.newBuilder()
            .setHeader(Header.newBuilder().build())
            .setOperation(Operation.newBuilder()
                            .setType(OperationType.GET)
                            .setName("someOperation")
                            .build())
            .build()
        String responseObject = "Sample response"
        byte [] sampleEncodedObject = "Some bytes".bytes

        when:
        KafkaMessage successMessage = kafkaResponseBuilder.buildSuccessResponse(request, responseObject)

        then:
//        1 * encoder.encode(responseObject) >> sampleEncodedObject
        successMessage.response.errorMessage == null
        successMessage.response.status == ResponseStatus.SUCCESS
        successMessage.response.data == responseObject
    }

    def "test BuildSuccessResponse operation name should be the same given in replyTo"() {
        given:
        KafkaMessage request = new KafkaMessage(
                header: new Header(
                        replyTo: "origin-topic",
                        destination: "destination-topic"
                ),
                operation: new Operation(
                        type: OperationType.GET,
                        name: "someOperation"
                ),
                replyTo: new Operation(
                        type: OperationType.RESPONSE,
                        name: "someOperation"
                )
        )

        when:
        KafkaMessage successMessage = kafkaResponseBuilder.buildSuccessResponse(request, "")

        then:
        successMessage.operation.type == request.replyTo.type
        successMessage.operation.name == request.replyTo.name
    }

    def "test BuildSuccessResponse should reply to the replyTo topic on the header"() {
        given:
        KafkaMessage request = new KafkaMessage(
                header: new Header(
                        replyTo: "origin-topic",
                        destination: "destination-topic"
                ),
                operation: new Operation(
                        type: OperationType.GET,
                        name: "someOperation"
                )
        )

        when:
        KafkaMessage successMessage = kafkaResponseBuilder.buildSuccessResponse(request, "")

        then:
        successMessage.header.replyTo == request.header.destination
        successMessage.header.destination == request.header.replyTo
    }

    def "test BuildSuccessResponse must keep company, div and user in response"() {
        given:
        KafkaMessage request = KafkaMessage.newBuilder()
                .setHeader(Header.newBuilder().build())
                .setOperation(Operation.newBuilder().build())
                .setCompany("company")
                .setDivision("division")
                .setUser("user")
                .build()

        when:
        KafkaMessage successMessage = kafkaResponseBuilder.buildSuccessResponse(request, "")

        then:
        successMessage.company == request.company
        successMessage.division == request.division
        successMessage.user == request.user
    }

    def "test BuildFailResponse should generate FAIL message with error message"() {
        given:
        KafkaMessage request = KafkaMessage.newBuilder()
                .setHeader(Header.newBuilder().build())
                .setOperation(Operation.newBuilder()
                .setType(OperationType.GET)
                .setName("someOperation")
                .build()
        ).build()
        String errorStr = "Sample response"

        when:
        KafkaMessage successMessage = kafkaResponseBuilder.buildFailResponse(request, errorStr)

        then:
        successMessage.response.errorMessage == errorStr
        successMessage.response.status == ResponseStatus.FAIL
        !successMessage.response.data
    }

}
