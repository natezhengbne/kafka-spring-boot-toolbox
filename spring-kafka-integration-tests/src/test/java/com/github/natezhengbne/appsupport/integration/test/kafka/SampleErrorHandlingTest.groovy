package com.github.natezhengbne.appsupport.integration.test.kafka

import com.github.natezhengbne.toolbox.exceptions.KafkaUnknownConfigurationException
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage
import com.github.natezhengbne.toolbox.kafka.message.ResponseStatus
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
class SampleErrorHandlingTest extends Specification {

    @Autowired
    KafkaEmbedded kafkaEmbedded

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry

    @Autowired
    SampleErrorHandlingClient sampleErrorHandlingClient

    void setup() {
        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
            ContainerTestUtils.waitForAssignment(messageListenerContainer, 2)
        }
    }

    @WithMockKeycloakUser
    def "test sendGetMessageToGetException"() {
        given:
        CompletableFuture<KafkaMessage> completableFutureString =  new CompletableFuture<>()
        sampleErrorHandlingClient.setCompletableFutureString(completableFutureString)

        when:
        sampleErrorHandlingClient.sendGetMessageToGenerateError()
        KafkaMessage receivedMessage = completableFutureString.get(20, TimeUnit.SECONDS)

        then:
        receivedMessage
        receivedMessage.response.status == ResponseStatus.FAIL
        receivedMessage.response.errorMessage.contains("IndexOutOfBoundsException")
    }

    @WithMockKeycloakUser
    def "test sendGetMessageToUnknownMessageConfiguration"() {
        given:
        CompletableFuture<KafkaMessage> completableFutureString =  new CompletableFuture<>()
        sampleErrorHandlingClient.setCompletableFutureString(completableFutureString)

        when:
        sampleErrorHandlingClient.sendGetMessageToUnknownMessageConfiguration()
        KafkaMessage receivedMessage = completableFutureString.get(10, TimeUnit.SECONDS)

        then:
        thrown(KafkaUnknownConfigurationException)
    }

    @WithMockKeycloakUser
    def "test sendGetMessageToUnknownRequestMethodHandler"() {
        given:
        CompletableFuture<KafkaMessage> completableFutureString =  new CompletableFuture<>()
        sampleErrorHandlingClient.setCompletableFutureString(completableFutureString)

        when:
        sampleErrorHandlingClient.sendGetMessageToUnknownRequestMethodHandler()
        KafkaMessage receivedMessage = completableFutureString.get(100, TimeUnit.SECONDS)

        then:
        receivedMessage
        receivedMessage.response.status == ResponseStatus.FAIL
        receivedMessage.response.errorMessage.contains("KafkaMethodNotFoundException")
    }
}
