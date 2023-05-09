package com.lets.kkiri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MessageController {

	@GetMapping("/chat")
	public String chatGet() {
		log.info("@ChatController, chat GET()");
		return null;
	}
}
