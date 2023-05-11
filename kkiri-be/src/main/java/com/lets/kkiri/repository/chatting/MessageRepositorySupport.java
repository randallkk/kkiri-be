package com.lets.kkiri.repository.chatting;

import java.time.LocalDateTime;
import java.util.List;


import com.lets.kkiri.entity.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MessageRepositorySupport {

	private final MongoTemplate mongoTemplate;

	public Page<Message> findRecent(Long moimId, Pageable pageable) {
		Query query = new Query();
		query.addCriteria(Criteria.where("moimId").is(moimId));
		query.with(Sort.by(Sort.Direction.DESC, "time"));
		query.with(pageable);
		List<Message> messages = null;
		try {
			messages = mongoTemplate.find(query, Message.class);
		} catch (Exception ex) {
		}
			return PageableExecutionUtils.getPage(
			messages,
			pageable,
			() -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), Message.class)
		);
	}

}
