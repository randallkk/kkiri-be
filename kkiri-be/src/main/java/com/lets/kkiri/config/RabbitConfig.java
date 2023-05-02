package com.lets.kkiri.config;

import javax.validation.constraints.Null;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableRabbit
public class RabbitConfig {

	private static final String CHAT_QUEUE_NAME = "chat.queue";
	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "room.*";

	//Queue 등록
	@Bean
	public Queue queue() {
		return new Queue(CHAT_QUEUE_NAME, true);
	}

	//Exchange 등록
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(CHAT_EXCHANGE_NAME);
	}

	//Exchange와 Queue 바인딩
	@Bean
	public Binding binding() {
		return BindingBuilder.bind(queue()).to(exchange()).with(ROUTING_KEY);
	}

	@Bean
	public Module dateTimeModule() {
		return new JavaTimeModule();
	}

	//Spring에서 자동 생성해주는 ConnectionFactory는 SimpleConnectionFactory
	//여기서 사용하는 건 CachingConnectionFactory라 새로 등록
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost("k8a606.p.ssafy.io");
		factory.setUsername("kkiri");
		factory.setPassword("lets");
		factory.setPort(8672);
		return factory;
	}

	/**
	 * messageConverter를 커스터마이징 하기 위해 Bean 새로 등록
	 */
	@Bean
	public RabbitTemplate rabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
		return rabbitTemplate;
	}

	@Bean
	public SimpleMessageListenerContainer container() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(CHAT_QUEUE_NAME);
		// container.setMessageListener(null);
		return container;
	}

	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		//LocalDatetime serializable을 위해
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
		objectMapper.registerModule(dateTimeModule());
		Jackson2JsonMessageConverter converter =  new Jackson2JsonMessageConverter(objectMapper);

		return converter;
	}
}
