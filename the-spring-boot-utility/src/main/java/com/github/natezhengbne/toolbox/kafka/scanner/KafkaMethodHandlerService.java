package com.github.natezhengbne.toolbox.kafka.scanner;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.exceptions.KafkaMethodNotFoundException;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KafkaMethodHandlerService {

    @Getter @Setter
    private Map<OperationKey, Method> operationsMapping;

    @Autowired
    private KafkaAnnotationScanner scanner;

    @PostConstruct
    protected void scanHandlerMappings() {
        operationsMapping = new HashMap<>();
        List<Method> methods = scanner.getKafkaMappingAnnotatedMethods();
        for (Method method : methods) {
            operationsMapping.put(getTargetOperation(method), method);
        }
    }

    public Method getHandlerMethod(OperationType operationType, String operationName) {
        Method method = operationsMapping.get(new OperationKey(operationType, operationName));
        if (method == null) {
            String message = "The method type: "+ operationType + " name: " + operationName + " was not found";
            throw  new KafkaMethodNotFoundException(message);
        }
        return operationsMapping.get(new OperationKey(operationType, operationName));
    }

    protected OperationKey getTargetOperation(Method method) {
        KafkaMapping kafkaMapping = method.getAnnotation(KafkaMapping.class);
        return new OperationKey(kafkaMapping.operationType(), kafkaMapping.operation());
    }
}
