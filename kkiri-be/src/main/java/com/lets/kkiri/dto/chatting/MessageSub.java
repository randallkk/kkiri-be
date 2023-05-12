package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageSub {
	private MoimSessionReq.MoimSessionType messageType;
	private Long moimId;
	private String memberKakaoId;
	private String nickname;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime time;

	public static MessageSub messageToDto(Message msg) {
		MessageSub sub = new MessageSub();
		sub.moimId = msg.getMoimId();
		sub.memberKakaoId = msg.getMemberKakaoId();
		sub.nickname = msg.getNickname();
		sub.message = msg.getMessage();
		sub.time = msg.getTime();
		sub.messageType = msg.getMessageType();
		return sub;
	}

	public static MessageSub messageDtoToSub(MessageDto msg) {
		MessageSub sub = new MessageSub();
		sub.moimId = msg.getMoimId();
		sub.memberKakaoId = msg.getMemberKakaoId();
		sub.nickname = msg.getNickname();
		sub.message = msg.getMessage();
		sub.time = msg.getTime();
		return sub;
	}
}
