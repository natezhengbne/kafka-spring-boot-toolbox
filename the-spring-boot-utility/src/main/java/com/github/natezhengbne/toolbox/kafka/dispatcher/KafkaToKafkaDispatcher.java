package com.github.natezhengbne.toolbox.kafka.dispatcher;

import com.github.natezhengbne.toolbox.kafka.scanner.KafkaMethodHandlerService;
import com.github.natezhengbne.toolbox.kafka.util.ObjectParser;
import com.github.natezhengbne.toolbox.security.UserCredentials;
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import com.github.natezhengbne.toolbox.kafka.util.response.KafkaResponseBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class KafkaToKafkaDispatcher {

    @Autowired
    KafkaMethodHandlerService kafkaMethodHandlerService;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ObjectParser objectParser;

    @Autowired
    KafkaResponseBuilder responseBuilder;

    @Autowired
    UserCredentials credentials;

    public Optional<KafkaMessage> dispatch(KafkaMessage data) throws Exception{
//        setTokenIfNotSet(data);
        Method method = kafkaMethodHandlerService.getHandlerMethod(data.getOperation().getType(), data.getOperation().getName());
        List<Object> parametersList = objectParser.parseMethodParameters(data, method);
        Object result = method.invoke(applicationContext.getBean(method.getDeclaringClass()), parametersList.toArray());
        if(data.getReplyTo() == null) return Optional.empty();
        return Optional.of(responseBuilder.buildSuccessResponse(data, result));
    }

//    private void setTokenIfNotSet(KafkaMessage request) {
//        if((credentials.getToken() == null) || credentials.getToken().isEmpty() || credentials.getToken()!=request.getHeader().getToken()) {
//            credentials.setToken(request.getHeader().getToken());
//        }
//    }
}
