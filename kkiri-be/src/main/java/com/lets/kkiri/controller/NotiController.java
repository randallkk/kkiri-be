package com.lets.kkiri.controller;

import com.lets.kkiri.config.jwt.JwtTokenUtil;
import com.lets.kkiri.dto.noti.HelpNotiReq;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.dto.noti.RouteGuideNotiReq;
import com.lets.kkiri.service.FcmService;
import com.lets.kkiri.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notiService;
    @PostMapping("/presses")
    public ResponseEntity<?> sendPressNoti(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody PressNotiReq pressNotiReq
    ) {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendPressNoti(senderKakaoId, pressNotiReq.getReceiverKakaoId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps")
    public ResponseEntity<?> sendHelpNoti(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody HelpNotiReq helpNotiReq
    ) {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendHelpNoti(senderKakaoId, helpNotiReq.getChatRoomId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps/guides")
    public ResponseEntity<?> sendRouteGuide(
            @RequestHeader(JwtTokenUtil.HEADER_STRING) String token,
            @RequestBody RouteGuideNotiReq routeGuideReq)
    {
        String senderKakaoId = JwtTokenUtil.getIdentifier(token);
        notiService.sendRoute(senderKakaoId, routeGuideReq);
        return ResponseEntity.ok().build();
    }
}
