package com.github.natezhengbne.appsupport.integration.test.kafka;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaResponse;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;

@Slf4j
@KafkaController
public class SampleNoParameterClient {

    @Getter @Setter
    private CompletableFuture<String> completableFutureString;

    @Autowired
    KafkaRequestSender sender;

    public void sendGetMessageWithNoParameter() {
        sender.sendMessage("getMessage", null);
    }

    @KafkaMapping(operationType = OperationType.RESPONSE, operation = "message")
    public void listenGetMessageWithNoParameterResponse(@KafkaResponse String sampleMessage) throws Exception{
        Thread.sleep(100);
        completableFutureString.complete(sampleMessage.toString());
    }
}
