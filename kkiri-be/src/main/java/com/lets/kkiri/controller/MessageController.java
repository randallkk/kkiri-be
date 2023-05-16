package com.lets.kkiri.controller;

import com.lets.kkiri.dto.auth.ReissueGetRes;
import com.lets.kkiri.dto.chatting.MessageRes;
import com.lets.kkiri.service.MessageRoomService;
import com.lets.kkiri.service.MessageService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class MessageController {

	private final MessageRoomService messageRoomService;

	@GetMapping()
	public ResponseEntity getChat(@RequestParam Long moimId,
		@RequestParam(required = false) String lastMessageId,
		@PageableDefault(size=30, page = 0) Pageable pageable) {
		if(lastMessageId == null) {
			MessageRes firstChat = messageRoomService.getFirstChat(moimId, pageable);
			if(firstChat != null) {
				return ResponseEntity.status(200).body(firstChat);
			}
		}
		else{
			MessageRes chat = messageRoomService.getChat(moimId, lastMessageId, pageable);
			if(chat != null) {
				return ResponseEntity.status(200).body(chat);
			}
		}
		return ResponseEntity.ok().body(null);
	}
}
