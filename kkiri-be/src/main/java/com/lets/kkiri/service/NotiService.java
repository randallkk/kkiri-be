package com.lets.kkiri.service;

import com.lets.kkiri.dto.noti.NotiLogDto;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberDevice;
import com.lets.kkiri.repository.member.MemberDeviceRepository;
import com.lets.kkiri.repository.member.MemberDeviceRepositorySupport;
import com.lets.kkiri.repository.member.MemberRepository;
import com.lets.kkiri.repository.noti.NotiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotiService {
    private final NotiLogRepository notiLogRepository;
    private final MemberRepository memberRepository;
    private final MemberDeviceRepositorySupport memberDeviceRepositorySupport;
    private final FcmService fcmService;
    public void sendPressNoti(PressNotiReq pressNotiReq) {
        Member recvMember = memberRepository.findByEmail(pressNotiReq.getRecieverEmail()).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );

        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMemberId(recvMember.getId());
        List<NotiLogDto> sucessLogList = new ArrayList<>();
        try {
            sucessLogList =  fcmService.sendMessage(
                    tokenList,
                    "재촉 알림",
                    pressNotiReq.getSenderEmail() + "님이 재촉 중입니다.");
        } catch (IOException e){
            log.error("FCM ERROR");
        }

        sucessLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity());
        });
    }
}
