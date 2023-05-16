package com.lets.kkiri.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;

	@Bean
	Queue chatQueue() {
		return new Queue("chat.queue", false);
	}
	@Bean
	Queue gpsQueue() {
		return new Queue("gps.queue", false);
	}

	@Bean
	DirectExchange chatDirectExchange() {
		return new DirectExchange("chat.exchange");
	}
	@Bean
	DirectExchange gpsDirectExchange() {
		return new DirectExchange("gps.exchange");
	}

	@Bean
	Binding chatBinding(Queue chatQueue, DirectExchange chatDirectExchange) {
		return BindingBuilder.bind(chatQueue).to(chatDirectExchange).with("chat.key");
	}
	@Bean
	Binding gpsBinding(Queue gpsQueue, DirectExchange gpsDirectExchange) {
		return BindingBuilder.bind(gpsQueue).to(gpsDirectExchange).with("gps.key");
	}

	@Bean
	RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	@Bean
	MessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
		final SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter());
		return factory;
	}

	@Bean
	ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setHost(host);
		connectionFactory.setPort(port);
		connectionFactory.setUsername(username);
		connectionFactory.setPassword(password);
		System.out.println("host: "+host);
		return connectionFactory;
	}
}
