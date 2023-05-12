package com.lets.kkiri.dto.chatting;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageMetaData {
	String lastMessageId;
	boolean last;
}
