package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessagePub {
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime time;
}
