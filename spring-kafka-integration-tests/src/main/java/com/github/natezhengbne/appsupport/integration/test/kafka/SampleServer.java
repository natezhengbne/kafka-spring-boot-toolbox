package com.github.natezhengbne.appsupport.integration.test.kafka;


import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaParam;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@KafkaController
public class SampleServer {

    @KafkaMapping(operationType = OperationType.GET, operation = "message")
    public String getMessageWithNoParameters() {
        return ("Sample Message");
    }

    @KafkaMapping(operationType = OperationType.GET, operation = "messageWithNumber")
    public String getMessageWithSingleParameter(@KafkaParam("number") Integer number) {
        return ("Test - " + number);
    }

    @KafkaMapping(operationType = OperationType.GET, operation = "getKafkaMessage")
    public String getKafkaMessage() {
        return ("Test - ");
    }

    @KafkaMapping(operationType = OperationType.GET, operation = "messageWithError")
    public String getMessageWithError() {
        throw new IndexOutOfBoundsException("Sample Exception");
    }

}