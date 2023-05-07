package com.lets.kkiri.service;

import com.lets.kkiri.entity.Message;
import com.lets.kkiri.repository.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

//    private final MongoTemplate mongoTemplate;
    private final ChatRepository chatRepository;

    public void chatInsert(Message mongoMessage) {
//        mongoTemplate.insert(mongoMessage);
        chatRepository.insert(mongoMessage);
    }
}
