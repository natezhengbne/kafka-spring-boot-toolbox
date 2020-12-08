package com.github.natezhengbne.appsupport.integration.test.kafka;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@Slf4j
@KafkaController
public class KafkaMessageInjectionClient {

    @Getter @Setter
    private CompletableFuture<KafkaMessage> completableFutureString;

    @Autowired
    private KafkaRequestSender requestSender;

    public void sendMessageToRetrieveKafkaMessage() {
        requestSender.sendMessage("getKafkaMessage", null);
    }

    @KafkaMapping(operationType = OperationType.RESPONSE, operation = "responseOperation")
    public void listenSendMessageToRetrieveKafkaMessageResponse(KafkaMessage message) {
        completableFutureString.complete(message);
    }
}
