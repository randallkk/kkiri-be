package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

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
@ToString
public class MessageSub {
	private Long moimId;
	private String memberKakaoId;
	private String nickname;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime regDate;
}
