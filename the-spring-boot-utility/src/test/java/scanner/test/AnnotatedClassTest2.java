package scanner.test;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;

@KafkaController
public class AnnotatedClassTest2 {

    @KafkaMapping(operationType = OperationType.GET, operation = "operation2")
    public String testMethod2() {
        return "test2";
    }
}
