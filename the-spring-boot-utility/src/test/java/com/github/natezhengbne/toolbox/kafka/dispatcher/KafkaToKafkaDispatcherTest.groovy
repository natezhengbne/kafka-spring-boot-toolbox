package com.github.natezhengbne.toolbox.kafka.dispatcher


import com.github.natezhengbne.toolbox.kafka.message.Header
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage
import com.github.natezhengbne.toolbox.kafka.message.Operation
import com.github.natezhengbne.toolbox.kafka.message.OperationType
import com.github.natezhengbne.toolbox.kafka.message.Response
import com.github.natezhengbne.toolbox.kafka.message.ResponseStatus
import org.springframework.context.ApplicationContext
import spock.lang.Specification

import java.lang.reflect.Method
import java.nio.ByteBuffer

class KafkaToKafkaDispatcherTest extends Specification {

    KafkaToKafkaDispatcher dispatcher

    com.github.natezhengbne.toolbox.kafka.scanner.KafkaMethodHandlerService methodHandlerService
    ApplicationContext applicationContext
    com.github.natezhengbne.toolbox.kafka.util.ObjectParser objectParser
    com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder responseBuilder
    com.github.natezhengbne.toolbox.security.UserCredentials credentials

    void setup() {
        methodHandlerService = Mock(com.github.natezhengbne.toolbox.kafka.scanner.KafkaMethodHandlerService)
        applicationContext = Mock(ApplicationContext)
        objectParser  = Mock(com.github.natezhengbne.toolbox.kafka.util.ObjectParser)
        responseBuilder = Mock(com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder)
        credentials = Mock(com.github.natezhengbne.toolbox.security.UserCredentials)
        dispatcher = new KafkaToKafkaDispatcher(methodHandlerService,applicationContext,objectParser,responseBuilder,credentials)
    }

    def "test dispatcher should invoke handler method and return result in message"() {
        given:
        List<String> operationParameters = ["Hello", "World"]
        KafkaMessage request = new KafkaMessage(
                header: new Header(
                        token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXZpc2lvbiI6IlFMRCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJyZWFkLWZvbyIsIndyaXRlIl0sImNvbXBhbnkiOiJNRVRFWFAiLCJleHAiOjE1MzgwNjM2NjEsInVzZXIiOiJqYXNvbnMiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjNjMDczOTYzLTJhNGQtNDA1OC05ZWJjLWZkNWFlMjZmOTUwYiIsImNsaWVudF9pZCI6ImRldmdsYW4tY2xpZW50In0.IoJgl7Jy5RyetpOfjaSXCvsErcnxlejdXzLeHQObMl0'
                ),
            operation: new Operation(
                type: OperationType.GET,
                name: "sampleOp1",
                params: ["input1" : operationParameters[0].bytes, "input2" : operationParameters[1].bytes ]
            ),
            replyTo: new Operation(
                type: OperationType.RESPONSE,
                name: "op3",
            )
        )
        Foo fooBean = new Foo()
        Method handlerMethod = Foo.getMethod("echo", String, String)

        String expectedOperationResult = operationParameters[0]+operationParameters[1]
        KafkaMessage expectedMessage = new KafkaMessage(
                header: new Header(
                        token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXZpc2lvbiI6IlFMRCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJyZWFkLWZvbyIsIndyaXRlIl0sImNvbXBhbnkiOiJNRVRFWFAiLCJleHAiOjE1MzgwNjM2NjEsInVzZXIiOiJqYXNvbnMiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjNjMDczOTYzLTJhNGQtNDA1OC05ZWJjLWZkNWFlMjZmOTUwYiIsImNsaWVudF9pZCI6ImRldmdsYW4tY2xpZW50In0.IoJgl7Jy5RyetpOfjaSXCvsErcnxlejdXzLeHQObMl0'
                ),
                response: new Response(
                    status: ResponseStatus.SUCCESS,
                    data: ByteBuffer.wrap(expectedOperationResult.bytes)
                )
        )

        when:
        Optional<KafkaMessage> message = dispatcher.dispatch(request)

        then:
        1 * methodHandlerService.getHandlerMethod(request.operation.type, request.operation.name) >> handlerMethod
        1 * objectParser.parseMethodParameters(request, handlerMethod) >> operationParameters
        1 * applicationContext.getBean(Foo) >> fooBean
        1 * responseBuilder.buildSuccessResponse(request, expectedOperationResult) >> expectedMessage
        message.isPresent()
        message.get().response == expectedMessage.response
    }

    def "test dispatcher should not send response if replyTo operation is not set"() {
        given:
        KafkaMessage request = new KafkaMessage(
                header: new Header(
                        token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkaXZpc2lvbiI6IlFMRCIsInVzZXJfbmFtZSI6InVzZXIiLCJzY29wZSI6WyJ0cnVzdCIsInJlYWQiLCJyZWFkLWZvbyIsIndyaXRlIl0sImNvbXBhbnkiOiJNRVRFWFAiLCJleHAiOjE1MzgwNjM2NjEsInVzZXIiOiJqYXNvbnMiLCJhdXRob3JpdGllcyI6WyJST0xFX0FETUlOIl0sImp0aSI6IjNjMDczOTYzLTJhNGQtNDA1OC05ZWJjLWZkNWFlMjZmOTUwYiIsImNsaWVudF9pZCI6ImRldmdsYW4tY2xpZW50In0.IoJgl7Jy5RyetpOfjaSXCvsErcnxlejdXzLeHQObMl0'
                ),
                operation: new Operation(
                        type: OperationType.DELETE,
                        name: "op3",
                        params: null
                )
        )
        Foo fooBean = new Foo()
        Method handlerMethod = Foo.getMethod("deleteAndDoNotSendResponse")

        when:
        Optional<KafkaMessage> message = dispatcher.dispatch(request)

        then:
        1 * methodHandlerService.getHandlerMethod(request.operation.type, request.operation.name) >> handlerMethod
        1 * objectParser.parseMethodParameters(request, handlerMethod) >> []
        1 * applicationContext.getBean(Foo) >> fooBean
        0 * responseBuilder.buildSuccessResponse(_,_)
        !message.isPresent()
    }

    private class Foo {

        @com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping(operationType = OperationType.GET, operation="op1")
        String echo (@com.github.natezhengbne.toolbox.kafka.annotation.KafkaParam("input1") String input1, @com.github.natezhengbne.toolbox.kafka.annotation.KafkaParam("input2") String input2) {
            return input1 + input2
        }

        @com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping(operationType = OperationType.GET, operation="op2")
        String createError () {
            throw new RuntimeException("Sample exception")
        }

        @com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping(operationType = OperationType.DELETE, operation="op3")
        void deleteAndDoNotSendResponse () {
            // Do something
        }

    }

}
