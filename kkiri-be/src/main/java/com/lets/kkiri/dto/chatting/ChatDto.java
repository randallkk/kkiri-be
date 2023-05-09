package com.lets.kkiri.dto.chatting;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

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
public class ChatDto {
	private Long roomId;
	private Long memberId;
	private String message;
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDateTime regDate;
}
