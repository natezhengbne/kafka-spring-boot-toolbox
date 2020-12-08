package com.github.natezhengbne.toolbox.kafka.scanner;

import com.github.natezhengbne.toolbox.annotation.scanner.BaseAnnotationScanner;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Setter
public class KafkaAnnotationScanner extends BaseAnnotationScanner {

    public List<Method> getKafkaMappingAnnotatedMethods() {
        return super.getMappingAnnotatedMethods(KafkaMapping.class, KafkaController.class);
    }

}
