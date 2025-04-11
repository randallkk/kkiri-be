package com.lets.kkiri.dto.chatting;

import com.lets.kkiri.dto.BaseMessageDto;

import com.lets.kkiri.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class MessageDto extends BaseMessageDto {
	private Long moimId;
	private String kakaoId;
	private String nickname;
	private String message;

	public MessageDto toDto(Message msg) {
		MessageDto dto = new MessageDto();
		dto.moimId = msg.getMoimId();
		dto.kakaoId = msg.getKakaoId();
		dto.nickname = msg.getNickname();
		dto.message = msg.getMessage();
		return dto;
	}
}
