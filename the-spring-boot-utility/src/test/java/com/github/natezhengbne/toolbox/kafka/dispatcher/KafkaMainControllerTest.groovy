package com.github.natezhengbne.toolbox.kafka.dispatcher


import spock.lang.Specification

class KafkaMainControllerTest extends Specification {

    KafkaMainController kafkaMainController

    KafkaToKafkaDispatcher kafkaToKafkaDispatcher
//    KafkaTemplate kafkaTemplate
    com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender kafkaRequestSender

    void setup() {
        kafkaToKafkaDispatcher = Mock(KafkaToKafkaDispatcher)
        kafkaRequestSender = Mock(com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender)
//        kafkaTemplate = Mock(KafkaTemplate)
        kafkaMainController = new KafkaMainController(kafkaToKafkaDispatcher, kafkaRequestSender)
    }

    def "test process should dispatch message and send response"() {
        given:
        com.github.natezhengbne.toolbox.kafka.message.KafkaMessage kafkaMessage = new com.github.natezhengbne.toolbox.kafka.message.KafkaMessage(
                header: new com.github.natezhengbne.toolbox.kafka.message.Header(replyTo: "responseTopic"),
                operation: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: com.github.natezhengbne.toolbox.kafka.message.OperationType.GET,
                        name: "testOperation",
                        params: null
                ),
                replyTo: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: com.github.natezhengbne.toolbox.kafka.message.OperationType.RESPONSE,
                        name: "testOperation",
                )

        )

        com.github.natezhengbne.toolbox.kafka.message.KafkaMessage response = new com.github.natezhengbne.toolbox.kafka.message.KafkaMessage(
                header: new com.github.natezhengbne.toolbox.kafka.message.Header(destination: kafkaMessage.getHeader().getReplyTo()),
                operation: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: com.github.natezhengbne.toolbox.kafka.message.OperationType.GET,
                        name: "responseOperation",
                        params: null
                )
        )

        when:
        kafkaMainController.process(kafkaMessage)

        then:
        1 * kafkaToKafkaDispatcher.dispatch(kafkaMessage) >> Optional.of(response)
//        1 * kafkaTemplate.send(response.header.destination.toString(), response)
        1 * kafkaRequestSender.response(response)
    }

    def "test process should dispatch message and do not send response if no operation is set"() {
        given:
        com.github.natezhengbne.toolbox.kafka.message.KafkaMessage kafkaMessage = new com.github.natezhengbne.toolbox.kafka.message.KafkaMessage(
                header: new com.github.natezhengbne.toolbox.kafka.message.Header(replyTo: "responseTopic"),
                operation: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: com.github.natezhengbne.toolbox.kafka.message.OperationType.GET,
                        name: "testOperation",
                        params: null
                )
        )

        when:
        kafkaMainController.process(kafkaMessage)

        then:
        1 * kafkaToKafkaDispatcher.dispatch(kafkaMessage) >> Optional.empty()
        0 * _
    }
}
