package com.lets.kkiri.controller;

import com.lets.kkiri.dto.moim.MoimPostReq;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.service.MoimService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/moims")
@RequiredArgsConstructor
public class MoimController {
    private final MoimService moimService;
    @PostMapping()
    public ResponseEntity<?> moinAdd(@RequestBody MoimPostReq moimPostReq) {
        moimService.addMoim(moimPostReq);
        return ResponseEntity.ok().build();
    }
}
