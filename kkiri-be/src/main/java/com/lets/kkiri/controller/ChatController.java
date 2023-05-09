package com.lets.kkiri.controller;

import java.time.LocalDateTime;

import com.lets.kkiri.entity.Message;
import com.lets.kkiri.service.ChatService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.lets.kkiri.dto.chatting.ChatDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

	@GetMapping("/chat")
	public String chatGet() {
		log.info("@ChatController, chat GET()");
		return
	}
}
