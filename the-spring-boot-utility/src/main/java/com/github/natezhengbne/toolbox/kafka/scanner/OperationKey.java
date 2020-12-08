package com.github.natezhengbne.toolbox.kafka.scanner;

import com.github.natezhengbne.toolbox.kafka.message.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperationKey {
    private OperationType type;
    private String name;
}
