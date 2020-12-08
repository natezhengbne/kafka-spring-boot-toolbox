package com.github.natezhengbne.toolbox.kafka.util.request;

import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import lombok.Data;

@Data
public class RequestConfig {
    private String toTopic;
    private OperationType ToOperationType;
    private String toOperationName;
    private String replyToTopic;
    private OperationType replyToOperationType;
    private String replyToOperationName;
}
