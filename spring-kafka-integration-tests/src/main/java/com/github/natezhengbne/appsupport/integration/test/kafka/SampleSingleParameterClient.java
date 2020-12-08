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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@KafkaController
public class SampleSingleParameterClient {

    @Getter @Setter
    private CompletableFuture<String> completableFutureString;

    @Autowired
    KafkaRequestSender sender;

    public void sendGetMessageWithNumber(Integer number) {
        Map<String, Object> params = new HashMap<>();
        params.put("number", number);
        sender.sendMessage("getMessageWithNumber", params);
    }

    @KafkaMapping(operationType = OperationType.RESPONSE, operation = "messageWithNumber")
    public void listenGetMessageWithNumberResponse(@KafkaResponse String sampleMessage) {
        completableFutureString.complete(sampleMessage);
    }
}
