package com.lets.kkiri.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chat")
public class ChatRoomController {

	@GetMapping("/room")
	public String getRoom(Long chatRoomId, String nickname, Model model) {
		model.addAttribute("chatRoomId", chatRoomId);
		model.addAttribute("nickname", nickname);
		return "chat/room";
	}
}
