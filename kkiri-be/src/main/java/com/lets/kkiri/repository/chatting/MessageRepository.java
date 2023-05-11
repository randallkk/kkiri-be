package com.lets.kkiri.repository.chatting;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.lets.kkiri.entity.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
