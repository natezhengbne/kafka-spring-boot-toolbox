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
public class SampleErrorHandlingClient {

    @Getter @Setter
    private CompletableFuture<KafkaMessage> completableFutureString;

    @Autowired
    KafkaRequestSender sender;

    public void sendGetMessageToGenerateError() {
        sender.sendMessage("generateKafkaError", null);
    }

    @KafkaMapping(operationType = OperationType.RESPONSE, operation = "messageWithError")
    public void listenMessageWithErrorResponse(KafkaMessage message) throws Exception{
        Thread.sleep(2000);
        completableFutureString.complete(message);
    }

    public void sendGetMessageToUnknownMessageConfiguration() {
        sender.sendMessage("unknown", null);
    }

    public void sendGetMessageToUnknownRequestMethodHandler() {
        sender.sendMessage("unknownRequestMethod", null);
    }
}
