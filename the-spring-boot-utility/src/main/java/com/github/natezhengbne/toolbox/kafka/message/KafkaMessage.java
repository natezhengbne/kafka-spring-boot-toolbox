package com.github.natezhengbne.toolbox.kafka.message;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class KafkaMessage {
	private Header header;
	private Operation operation;
	private Operation replyTo;
	private Response response;
}
