package com.lets.kkiri.controller;

import java.time.LocalDateTime;

import com.lets.kkiri.entity.Message;
import com.lets.kkiri.service.ChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
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

	@Qualifier("chatRabbitTemplate")
	private final RabbitTemplate template;

	private final ChatService chatService;

	private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
	private final static String CHAT_QUEUE_NAME = "chat.queue";

	@MessageMapping("chat.enter.{roomId}")
	public void enter(ChatDto chatDto, @DestinationVariable String roomId) {
		chatDto.setMessage("입장하셨습니다.");
		chatDto.setRegDate(LocalDateTime.now());

		//exchange
		template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomId, chatDto); //exchange

		//DB에 넣기
		chatService.chatInsert(Message.builder()
						.message(chatDto.getMessage())
						.time(chatDto.getRegDate())
						.memberId(chatDto.getMemberId())
//						.roomId(chatDto.getRoomId())
						.build());
	}

	@MessageMapping("chat.message.{roomId}")
	public void send(ChatDto chatDto, @DestinationVariable String roomId) {
		chatDto.setRegDate(LocalDateTime.now());

		template.convertAndSend(CHAT_EXCHANGE_NAME, "room."+roomId, chatDto); //exchange
	}

	//receive()는 단순히 큐에 들어온 메시지를 소비만 한다(디버그용)
	@RabbitListener(queues = CHAT_QUEUE_NAME)
	public void receive(ChatDto chatDto){
		log.info("chatDto.getMessage() = {}", chatDto.getMessage());
	}

//	@PostMapping("/chat")
//	public void chat(@RequestBody ChatDto chatDto) {
//		chatDto.setMessage("입장하셨습니다.");
//		chatDto.setRegDate(LocalDateTime.now());
//
//		chatService.mongoInsert(MongoMessage.builder()
//				.message(chatDto.getMessage())
//				.time(chatDto.getRegDate())
////						.memberId(chatDto.getMemberId())
////						.roomId(chatDto.getRoomId())
//				.build());
//	}

}
