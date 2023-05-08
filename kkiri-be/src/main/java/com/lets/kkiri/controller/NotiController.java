package com.lets.kkiri.controller;

import com.lets.kkiri.dto.noti.HelpNotiReq;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.dto.noti.RouteGuideNotiReq;
import com.lets.kkiri.service.FcmService;
import com.lets.kkiri.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/noti")
@RequiredArgsConstructor
public class NotiController {
    private final NotiService notiService;
    @PostMapping("/presses")
    public ResponseEntity<?> sendPressNoti(@RequestBody PressNotiReq pressNotiReq) {
        notiService.sendPressNoti(pressNotiReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps")
    public ResponseEntity<?> sendHelpNoti(@RequestBody HelpNotiReq helpNotiReq) {
        notiService.sendHelpNoti(helpNotiReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/helps/guides")
    public ResponseEntity<?> sendRouteGuide(@RequestBody RouteGuideNotiReq routeGuideReq) {
        notiService.sendRoute(routeGuideReq);
        return ResponseEntity.ok().build();
    }
}
