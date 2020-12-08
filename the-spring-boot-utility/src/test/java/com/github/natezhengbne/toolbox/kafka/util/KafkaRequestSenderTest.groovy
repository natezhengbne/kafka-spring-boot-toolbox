package com.github.natezhengbne.toolbox.kafka.util


import org.springframework.kafka.core.KafkaTemplate
import spock.lang.Specification

class KafkaRequestSenderTest extends Specification {

    com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender kafkaRequestSender

    Map<String, com.github.natezhengbne.toolbox.kafka.util.request.RequestConfig> requestConfigMap
    KafkaTemplate<String, com.github.natezhengbne.toolbox.kafka.message.KafkaMessage> kafkaTemplate
    ObjectEncoder encoder
    com.github.natezhengbne.toolbox.security.UserCredentials credentials

    String company = "METEXP"
    String division = "QLD"
    String user = "USER"

    def setup() {
        requestConfigMap = [
                listClient   : new com.github.natezhengbne.toolbox.kafka.util.request.RequestConfig(
                        toTopic: "topic-pro-core-data",
                        toOperationType: com.github.natezhengbne.toolbox.kafka.message.OperationType.GET,
                        toOperationName: "listClient",
                        replyToTopic: "pro-f-int-server1",
                        replyToOperationType: com.github.natezhengbne.toolbox.kafka.message.OperationType.RESPONSE,
                        replyToOperationName: "listClient"
                ),
                addClientById: new com.github.natezhengbne.toolbox.kafka.util.request.RequestConfig(
                        toTopic: "topic-pro-core-data-2",
                        toOperationType: com.github.natezhengbne.toolbox.kafka.message.OperationType.ADD,
                        toOperationName: "client",
                        replyToTopic: "topic-pro-core-data-1",
                        replyToOperationType: com.github.natezhengbne.toolbox.kafka.message.OperationType.RESPONSE,
                        replyToOperationName: "listClient"
                ),
        ]
        encoder = Mock(ObjectEncoder)
        kafkaTemplate = Mock(KafkaTemplate)
        credentials = Mock(com.github.natezhengbne.toolbox.security.UserCredentials)
        kafkaRequestSender = new com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender(
                requestConfigMap: requestConfigMap,
                encoder: encoder,
                kafkaTemplate: kafkaTemplate,
                credentials: credentials,
        )
    }

    def "SendMessage"() {
        given: "Parameters"
        String id = "listClient"
        def params = ["clientId": 20, "clientName": "Bunnings"]

        and: "Expected generated message"
        def encodedParams = ["clientId": "Encoded B64 JSON".bytes, "clientName": "Encoded B64 JSON".bytes]
        com.github.natezhengbne.toolbox.kafka.message.KafkaMessage message =  new com.github.natezhengbne.toolbox.kafka.message.KafkaMessage(
                header: new com.github.natezhengbne.toolbox.kafka.message.Header(
                        destination: requestConfigMap[id].toTopic,
                        replyTo: requestConfigMap[id].replyToTopic
                ),
                company: company,
                division: division,
                user: user,
                operation: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: requestConfigMap[id].toOperationType,
                        name: requestConfigMap[id].toOperationName,
                        params: encodedParams
                ),
                replyTo: new com.github.natezhengbne.toolbox.kafka.message.Operation(
                        type: requestConfigMap[id].replyToOperationType,
                        name: requestConfigMap[id].replyToOperationName,
                        params: null
                ),
                response: null
        )

        when:
        kafkaRequestSender.sendMessage(id, params)

        then:
//        1 * encoder.encodeMap(params) >> encodedParams
//        1 * params >> encodedParams
//        1 * kafkaTemplate.send(requestConfigMap[id].toTopic, message)
        1 * credentials.getCompany() >> company
        1 * credentials.getDivision() >> division
        1 * credentials.getUser() >> user
    }

    def "SendMessage must throw exception if configuration is not set properly"() {
        given: "Parameters"
        String wrongId = "wrongId"

        when:
        kafkaRequestSender.sendMessage(wrongId, null)

        then:
        Exception e = thrown(RuntimeException)
        e.message.contains(wrongId)

    }
}