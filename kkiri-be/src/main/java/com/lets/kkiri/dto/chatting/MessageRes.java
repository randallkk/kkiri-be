package com.lets.kkiri.dto.chatting;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageRes {
	private MessageMetaData meta;
	private List<MessageSub> chat;
}
