package com.github.natezhengbne.toolbox.kafka.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {
	private String destination;
	private String replyTo;
}
