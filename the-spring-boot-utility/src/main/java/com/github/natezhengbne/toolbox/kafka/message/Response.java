package com.github.natezhengbne.toolbox.kafka.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
	private ResponseStatus status;
	private String errorMessage;
	private byte[] data;
}
