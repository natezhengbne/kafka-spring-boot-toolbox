package com.github.natezhengbne.toolbox.kafka.dispatcher

import com.github.natezhengbne.toolbox.kafka.message.Header
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage
import com.github.natezhengbne.toolbox.kafka.message.Operation
import com.github.natezhengbne.toolbox.kafka.message.OperationType
import com.github.natezhengbne.toolbox.kafka.message.Response
import com.github.natezhengbne.toolbox.kafka.message.ResponseStatus
import com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ListenerExecutionFailedException
import org.springframework.messaging.Message
import spock.lang.Specification

import java.lang.reflect.InvocationTargetException

class KafkaErrorHandlerTest extends Specification {

    KafkaResponseBuilder kafkaResponseBuilder
    KafkaTemplate kafkaTemplate
    KafkaErrorHandler kafkaErrorHandler


    void setup() {
        kafkaResponseBuilder = Mock(KafkaResponseBuilder)
        kafkaTemplate = Mock(KafkaTemplate)
        kafkaErrorHandler = new KafkaErrorHandler(kafkaResponseBuilder, kafkaTemplate)
    }

    def "test error handler"() {
        given:
        KafkaMessage kafkaMessage = new KafkaMessage(
                header: new Header(replyTo: "responseTopic"),
                operation: new Operation(
                        type: OperationType.GET,
                        name: "testOperation",
                        params: null
                )
        )

        KafkaMessage response = new KafkaMessage(
                header: new Header(destination: kafkaMessage.header.replyTo),
                operation: new Operation(
                        type: OperationType.RESPONSE,
                        name: "responseOperation",
                        params: null
                ),
                response: new Response(
                        status: ResponseStatus.FAIL,
                        errorMessage: "Problem with database connection",
                        data: null
                )
        )

        Message message = Mock(Message)
        Exception originalException = new NullPointerException()
        ListenerExecutionFailedException e = new ListenerExecutionFailedException("", new InvocationTargetException(originalException))

        when:
        kafkaErrorHandler.handleError(message, e)

        then:
        1 * message.getPayload() >> kafkaMessage
        1 * kafkaResponseBuilder.buildFailResponse(kafkaMessage, originalException.toString()) >> response
        1 * kafkaTemplate.send(response.header.destination, response)
        0 * _
    }
}
