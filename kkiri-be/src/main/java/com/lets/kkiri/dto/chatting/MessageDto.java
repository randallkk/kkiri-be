package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MessageDto {
	private Long moimId;
	private String memberKakaoId;
	private String nickname;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime time;

	public static MessageDto toDto(Message msg) {
		MessageDto dto = new MessageDto();
		dto.moimId = msg.getMoimId();
		dto.memberKakaoId = msg.getMemberKakaoId();
		dto.nickname = msg.getNickname();
		dto.message = msg.getMessage();
		dto.time = msg.getTime();
		return dto;
	}
}
