package com.lets.kkiri.controller;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import com.lets.kkiri.dto.chatting.ChatDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

	private final RabbitTemplate template;

	private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
	private final static String CHAT_QUEUE_NAME = "chat.queue";

	@MessageMapping("chat.enter.{chatRoomId}")
	public void enter(ChatDto chatDto, @DestinationVariable String chatRoomId) {
		chatDto.setMessage("입장하셨습니다.");
		chatDto.setRegDate(LocalDateTime.now());

		//exchange
		template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+chatRoomId, chatDto);
	}

	@RabbitListener(queues = CHAT_QUEUE_NAME)
	public void receive(ChatDto chatDto){
		log.info("chatDto.getMessage() = {}", chatDto.getMessage());
	}
}
