package com.github.natezhengbne.appsupport.integration.test.kafka

import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.rule.KafkaEmbedded
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@ContextConfiguration
@SpringBootTest
@EmbeddedKafka(partitions = 1,
        controlledShutdown = false,
        topics = ["request-topic", "response-topic"])
class SampleClientTest extends Specification {

    @Autowired
    KafkaEmbedded kafkaEmbedded

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    @Autowired
    SampleNoParameterClient sampleNoParameterClient

    @Autowired
    SampleSingleParameterClient sampleSingleParameterClient

    @Autowired
    KafkaMessageInjectionClient kafkaMessageInjectionClient

    void setup() {
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, 2)
        }
    }

    @WithMockKeycloakUser
    def "test sendGetMessageWithNoParameter"() {
        given:
        CompletableFuture<String> completableFutureString  =  new CompletableFuture<>()
        sampleNoParameterClient.setCompletableFutureString(completableFutureString)

        when:
        sampleNoParameterClient.sendGetMessageWithNoParameter()
        String receivedMessage = completableFutureString.get(20, TimeUnit.SECONDS)

        then:
        noExceptionThrown()
        receivedMessage == "Sample Message"

    }

    @WithMockKeycloakUser
    def "SendGetMessageRequestWithSingleParameter"() {
        given:
        CompletableFuture<String> completableFutureString =  new CompletableFuture<>()
        sampleSingleParameterClient.setCompletableFutureString(completableFutureString)

        when:
        sampleSingleParameterClient.sendGetMessageWithNumber(500)
        String receivedMessage = completableFutureString.get(10, TimeUnit.SECONDS)

        then:
        noExceptionThrown()
        receivedMessage == "Test - 500"

    }

    @WithMockKeycloakUser
    def "test sendGetMessageToRetrieveKafkaMessage"() {
        given:
        CompletableFuture<KafkaMessage> completableFutureString =  new CompletableFuture<>()
        kafkaMessageInjectionClient.setCompletableFutureString(completableFutureString)

        when:
        kafkaMessageInjectionClient.sendMessageToRetrieveKafkaMessage()
        KafkaMessage receivedMessage = completableFutureString.get(10, TimeUnit.SECONDS)

        then:
        noExceptionThrown()
        receivedMessage.class == KafkaMessage

    }
}
