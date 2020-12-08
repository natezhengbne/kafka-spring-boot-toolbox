package com.github.natezhengbne.toolbox.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
@Data
@AllArgsConstructor
@Builder
public class Operation {
	private OperationType type;
	private String name;
	private Map<String,byte[]> params;
}
