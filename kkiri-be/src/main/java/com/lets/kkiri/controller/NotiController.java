package com.lets.kkiri.controller;

import com.lets.kkiri.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notiService;
}
