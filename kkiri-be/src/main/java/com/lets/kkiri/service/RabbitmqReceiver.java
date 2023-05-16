package com.lets.kkiri.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.RpcClient.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class RabbitmqReceiver {

	@RabbitListener(queues = "chat.queue")
	public void chatConsume(Response response) {
		log.info("consume : {}", response);
	}

	@RabbitListener(queues = "gps.queue")
	public void gpsConsume(Response response) {
		log.info("consume : {}", response);
	}
}
