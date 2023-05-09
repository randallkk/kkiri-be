package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageDto {
	public enum MessageType {
		ENTER, TALK
	}

	private MessageType messageType;
	private Long moimId;
	private Long memberId;
	private String nickname;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime regDate;
}
