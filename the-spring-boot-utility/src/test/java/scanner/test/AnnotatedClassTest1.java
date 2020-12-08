package scanner.test;

import com.github.natezhengbne.toolbox.kafka.annotation.KafkaController;
import com.github.natezhengbne.toolbox.kafka.annotation.KafkaMapping;
import com.github.natezhengbne.toolbox.kafka.message.OperationType;

@KafkaController
public class AnnotatedClassTest1 {

    @KafkaMapping(operationType = OperationType.ADD, operation = "operationA")
    public String testMethodA() {
        return "testA";
    }

    @KafkaMapping(operationType = OperationType.ADD, operation = "operationB")
    public String testMethodB() {
        return "testB";
    }

    public String testMethodC() {
        return "testC";
    }

}
