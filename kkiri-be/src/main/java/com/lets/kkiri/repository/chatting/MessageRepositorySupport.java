package com.lets.kkiri.repository.chatting;

import java.time.LocalDateTime;
import java.util.List;


import com.lets.kkiri.entity.Message;

import org.bson.types.ObjectId;
import org.hibernate.annotations.Where;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MessageRepositorySupport {

	private final MongoTemplate mongoTemplate;

	public Page<Message> findRecent(Long moimId, Pageable pageable) {
		Query query = new Query();
		query.addCriteria(Criteria.where("moimId").is(moimId));
		query.with(Sort.by(Sort.Direction.DESC, "time").and(Sort.by(Sort.Direction.DESC, "_id")));
		query.with(pageable);
		List<Message> messages = mongoTemplate.find(query, Message.class);
		return PageableExecutionUtils.getPage(
		messages,
		pageable,
		() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Message.class)
		);
	}

	public Page<Message> findMessage(Long moimId, Pageable pageable, String lastMessageId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").lt(new ObjectId(lastMessageId)).and("moimId").is(moimId));
		query.with(Sort.by(Sort.Direction.DESC, "time").and(Sort.by(Sort.Direction.DESC, "_id")));
		query.with(pageable);
		List<Message> messages = mongoTemplate.find(query, Message.class);
		return PageableExecutionUtils.getPage(
			messages,
			pageable,
			() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Message.class)
		);
	}
}
