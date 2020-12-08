package com.github.natezhengbne.toolbox.kafka.dispatcher;

import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import com.github.natezhengbne.toolbox.kafka.util.request.KafkaRequestSender;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class KafkaMainController {

    @Autowired
    KafkaToKafkaDispatcher dispatcher;

//    @Autowired
//    KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    @Autowired
    KafkaRequestSender kafkaRequestSender;


    @KafkaListener(topics = "#{'${spring.kafka.topics}'.replaceAll('\\s+','').split(',')}", errorHandler = "kafkaErrorHandler")
    public void process(KafkaMessage data) throws Exception{
        Optional<KafkaMessage> response = dispatcher.dispatch(data);
        response.ifPresent(message -> kafkaRequestSender.response(message));
    }
    
}
