package com.lets.kkiri.service;

import com.lets.kkiri.config.websocket.MoimSessionHandler;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.dto.fcm.FcmMessageDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.dto.noti.HelpNotiReq;
import com.lets.kkiri.dto.noti.NotiLogDto;
import com.lets.kkiri.dto.noti.PressNotiReq;
import com.lets.kkiri.dto.noti.RouteGuideNotiReq;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.member.MemberDeviceRepositorySupport;
import com.lets.kkiri.repository.member.MemberRepository;
import com.lets.kkiri.repository.moim.MoimRepository;
import com.lets.kkiri.repository.noti.NotiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotiService {
    private final NotiLogRepository notiLogRepository;
    private final MoimRepository moimRepository;
    private final MemberRepository memberRepository;
    private final MemberDeviceRepositorySupport memberDeviceRepositorySupport;
    private final FcmService fcmService;
    private final MessageRoomService messageRoomService;
    private final MessageService messageService;

    public void sendPressNoti(String senderKakaoId, String receiverKakaoId) {
        Member recvMember = memberRepository.findByKakaoId(receiverKakaoId).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );

        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMemberId(recvMember.getId());
        if (tokenList.size() == 0) throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

        List<NotiLogDto> successLogList = new ArrayList<>();
        try {
            successLogList = fcmService.sendMessageToToken(
                    FcmMessageDto.builder()
                            .tokenList(tokenList)
                            .channelId("hurry")
                            .title("재촉 알림")
                            .body(senderKakaoId + "님이 재촉 중입니다.")
                            .build()
            );

            //재촉 메세지 채팅방에 전송
            MessageDto dto = MessageDto.builder()
                    .message(senderKakaoId+"님이 " + receiverKakaoId+"님을 재촉 중입니다.")
                    .build();
            messageRoomService.sendMessage(MoimSessionReq.MoimSessionType.URGENT, dto, messageService);

        } catch (IOException e) {
            log.error("FCM ERROR");
        }

        successLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity());
        });
    }

    public void sendHelpNoti(String senderKakaoId, Long chatRoomId) {
        Moim targetMoim = moimRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 모임이 존재하지 않습니다.")
        );

        Long senderId = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        ).getId();

        // 보내는 사람을 제외한 모임원들의 토큰들을 불러옴.
        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMoimId(senderId, targetMoim.getId().toString());
        if (tokenList.size() == 0) throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

        List<NotiLogDto> successLogList = new ArrayList<>();
        try {
            successLogList = fcmService.sendMessageToToken(
                    FcmMessageDto.builder()
                            .tokenList(tokenList)
                            .channelId("sos")
                            .title("도움 요청 알림")
                            .body(senderKakaoId + "님이 도움을 요청 중입니다.")
                            .build()
            );
        } catch (IOException e) {
            log.error("FCM ERROR");
        }

        successLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity());
        });
    }

    public void sendRoute(String senderKakaoId, RouteGuideNotiReq routeGuideReq) {
        Member recvMember = memberRepository.findByKakaoId(routeGuideReq.getReceiverKakaoId()).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );

        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMemberId(recvMember.getId());
        if (tokenList.size() == 0) throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

        List<NotiLogDto> successLogList = new ArrayList<>();
        try {
            successLogList = fcmService.sendMessageToToken(
                    FcmMessageDto.builder()
                            .tokenList(tokenList)
                            .channelId("path")
                            .title("길 안내 알림")
                            .body(senderKakaoId + "님이 길 안내 중입니다.")
                            .path(routeGuideReq.getPath())
                            .build()
            );
        } catch (IOException e) {
            log.error("FCM ERROR");
        }

        successLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity());
        });
    }
}
