package com.github.natezhengbne.toolbox.kafka.util.request;

import com.github.natezhengbne.toolbox.exceptions.KafkaUnknownConfigurationException;
import com.github.natezhengbne.toolbox.kafka.message.Operation;
import com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder;
import com.github.natezhengbne.toolbox.security.UserCredentials;
import com.github.natezhengbne.toolbox.kafka.message.Header;
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Data
@ConfigurationProperties(prefix = "message")
public class KafkaRequestSender {

    Map<String, RequestConfig> requestConfigMap;

    @Autowired
    ObjectEncoder encoder;

    @Autowired
    KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    UserCredentials credentials;
    
    @Autowired
	ObjectEncoder objectEncoder;

    public void sendMessage(String messageId, Map<String, Object> params) {
        RequestConfig config = requestConfigMap.get(messageId);
        if (config == null) {
            String message = "Not able to find a message configuration for '"+messageId+"'.";
            throw new KafkaUnknownConfigurationException(message);
        }
       
        // the set method will check check the type : validate(fields()[2], value);
        KafkaMessage message =  KafkaMessage.builder()
                .header(
                    Header.builder()
                    .destination(config.getToTopic())
                    .replyTo(config.getReplyToTopic())
                    .build()
                )
                .operation(
                    Operation.builder()
                    .type(config.getToOperationType())
                    .name(config.getToOperationName())
                    .params(objectEncoder.encodeMap(params))
                    .build()
                )
                .replyTo(
                    Operation.builder()
                    .type(config.getReplyToOperationType())
                    .name(config.getReplyToOperationName())
                    .params(null)
                    .build()
                )
                .response(null)
                .build();
        this.send(config.getToTopic(),message);
    }

    public void response(KafkaMessage message){
        if (message.getHeader().getDestination() == null) {
            String info = "Not able to find a Topic.";
            throw new KafkaUnknownConfigurationException(info);
        }
        this.send(message.getHeader().getDestination(),message);
    }

    private void send(String topic,KafkaMessage message){
        kafkaTemplate.send(topic,KafkaMessage.class.getName(),message);
    }

}