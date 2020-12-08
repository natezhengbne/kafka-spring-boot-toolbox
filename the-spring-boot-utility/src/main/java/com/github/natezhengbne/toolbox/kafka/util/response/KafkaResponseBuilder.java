package com.github.natezhengbne.toolbox.kafka.util.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.natezhengbne.toolbox.kafka.message.*;
import com.github.natezhengbne.toolbox.kafka.util.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaResponseBuilder {

    @Autowired
    ObjectEncoder encoder;

    @Autowired
    ObjectMapper objectMapper;

    public KafkaMessage buildSuccessResponse(KafkaMessage request, Object content) {
        KafkaMessage message = this.build(request);
        Response response = null;

        response = Response.builder()
                .status(ResponseStatus.SUCCESS)
                .errorMessage(null)
                .data(encoder.encode(content)).build();

        message.setResponse(response);
        return message;
    }

    public KafkaMessage buildFailResponse(KafkaMessage request, String errorMessage) {
        KafkaMessage message = build(request);
        Response response = Response.builder()
                .status(ResponseStatus.FAIL)
                .errorMessage(errorMessage)
                .data(null).build();
        message.setResponse(response);
        return message;
    }

    protected KafkaMessage build(KafkaMessage request) {
        Header header = Header.builder()
                .destination(request.getHeader().getReplyTo())
                .replyTo(request.getHeader().getDestination())
                .build();
        Operation operation = request.getReplyTo() == null ? null :
                Operation.builder()
                .type(request.getReplyTo().getType())
                .name(request.getReplyTo().getName())
                .build();
        return KafkaMessage.builder()
                .header(header)
                .operation(operation)
                .build();
    }
}
