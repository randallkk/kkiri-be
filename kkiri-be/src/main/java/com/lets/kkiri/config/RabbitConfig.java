package com.lets.kkiri.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitConfig {

	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private int port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;

	private static final String CHAT_QUEUE_NAME = "chat.queue";
	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String GPS_QUEUE_NAME = "gps.queue";
	private static final String GPS_EXCHANGE_NAME = "gps.exchange";
	private static final String ROUTING_KEY = "room.*";

	//Queue 등록
	@Bean
	public Queue chatQueue() {
		return new Queue(CHAT_QUEUE_NAME, true);
	}
	@Bean
	public Queue gpsQueue() { return new Queue(GPS_QUEUE_NAME, true); }

	//Exchange 등록
	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange(CHAT_EXCHANGE_NAME);
	}
	@Bean
	public TopicExchange gpsExchange() { return new TopicExchange(GPS_EXCHANGE_NAME); }

	//Exchange와 Queue 바인딩
	@Bean
	public Binding chatBinding() {
		return BindingBuilder.bind(chatQueue()).to(chatExchange()).with(ROUTING_KEY);
	}
	@Bean
	public Binding gpsBinding() { return BindingBuilder.bind(gpsQueue()).to(gpsExchange()).with(ROUTING_KEY); }

	@Bean
	public Module dateTimeModule() {
		return new JavaTimeModule();
	}

	// CachingConnectionFactory 새로 등록
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory factory = new CachingConnectionFactory();
		factory.setHost(host);
		factory.setPort(port);
		factory.setUsername(username);
		factory.setPassword(password);
		return factory;
	}

	/**
	 * messageConverter를 커스터마이징 하기 위해 Bean 새로 등록
	 */
	@Bean
	public RabbitTemplate chatRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		rabbitTemplate.setRoutingKey(CHAT_QUEUE_NAME);
		return rabbitTemplate;
	}
	@Bean
	public RabbitTemplate gpsRabbitTemplate() {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setMessageConverter(jsonMessageConverter());
		rabbitTemplate.setRoutingKey(GPS_QUEUE_NAME);
		return rabbitTemplate;
	}

	@Bean
	public SimpleMessageListenerContainer chatContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(CHAT_QUEUE_NAME);
		// container.setMessageListener(null);
		return container;
	}
	@Bean
	public SimpleMessageListenerContainer gpsContainer() {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory());
		container.setQueueNames(GPS_QUEUE_NAME);
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
