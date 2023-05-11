package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.lets.kkiri.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class MessageSub {
	private Long moimId;
	private String memberKakaoId;
	private String nickname;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime time;

	public static MessageSub toDto(Message msg) {
		return new MessageSub(
			msg.getMoimId(),
			msg.getMemberKakaoId(),
			msg.getNickname(),
			msg.getMessage(),
			msg.getTime());
	}
}
