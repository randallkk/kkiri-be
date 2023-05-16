package com.lets.kkiri.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqProducer {
	private final RabbitTemplate rabbitTemplate;

	public void send(String exchange, String routingKey, Object data) {
		rabbitTemplate.convertAndSend(exchange, routingKey, data);
	}
}
