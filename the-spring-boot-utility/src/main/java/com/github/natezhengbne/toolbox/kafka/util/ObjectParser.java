package com.github.natezhengbne.toolbox.kafka.util;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaResponse;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaParam;
import com.github.natezhengbne.toolbox.kafka.message.KafkaMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Slf4j
public class ObjectParser {

    @Autowired
    ObjectEncoder objectEncoder;

    public List<Object> parseMethodParameters(KafkaMessage message, Method method) {
        List<Object> parametersList = new ArrayList<>();
        if (method.getParameterCount() <= 0) return parametersList;
        for (Parameter parameter : method.getParameters()) {
            Object parameterObject = extractParameter(parameter, message);
            parametersList.add(parameterObject);
        }
        return parametersList;
    }


    protected Object extractParameter(Parameter parameter, KafkaMessage message){
        Object result = null;
        if(parameter.getAnnotation(KafkaParam.class) != null) {
            result = objectEncoder.decode(
            		message.getOperation().getParams().get(parameter.getAnnotation(KafkaParam.class).value())
		            ,parameter.getType());
        }
        else if(parameter.getAnnotation(KafkaResponse.class) != null) {
            result = message.getResponse().getData();
        }
        else if(parameter.getType() == KafkaMessage.class) {
            result = message;
        }

        if(result!=null){
            log.debug("ObjectParser.extractParameter's class type - "+result.getClass().getName());
        }
        return result;

    }
    


}
