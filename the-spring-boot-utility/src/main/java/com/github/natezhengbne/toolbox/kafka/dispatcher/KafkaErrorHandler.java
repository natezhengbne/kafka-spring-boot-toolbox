package com.github.natezhengbne.toolbox.kafka.dispatcher;

import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import com.github.natezhengbne.toolbox.exceptions.KafkaMethodNotFoundException;
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.KafkaListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component("kafkaErrorHandler")
@Slf4j
@AllArgsConstructor
public class KafkaErrorHandler implements KafkaListenerErrorHandler {

    @Autowired
    KafkaResponseBuilder responseBuilder;

    @Autowired
    KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Override
    public Object handleError(Message<?> message, ListenerExecutionFailedException e) {
        KafkaMessage kafkaMessage = (KafkaMessage) message.getPayload();
        if(kafkaMessage.getOperation().getType() == OperationType.RESPONSE) {
            log.error("Response could not be handled", e);
            return "";
        }

        KafkaMessage errorResponse = convertErrorToFailKafkaResponse(kafkaMessage, e);
        kafkaTemplate.send(errorResponse.getHeader().getDestination().toString(), errorResponse);
        return errorResponse;
    }

    protected KafkaMessage convertErrorToFailKafkaResponse (KafkaMessage kafkaMessage, ListenerExecutionFailedException e) {
        String originalException = null;

        if(e.getCause() instanceof InvocationTargetException) {
            originalException = e.getCause().getCause().toString();
        } else if(e.getCause() instanceof KafkaMethodNotFoundException) {
            originalException = e.getCause().toString();
        }
        return responseBuilder.buildFailResponse(kafkaMessage, originalException);
    }
}