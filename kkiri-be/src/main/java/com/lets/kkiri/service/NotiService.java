package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
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
import com.lets.kkiri.repository.MoimRepositorySupport;
import com.lets.kkiri.repository.member.MemberDeviceRepositorySupport;
import com.lets.kkiri.repository.member.MemberRepository;
import com.lets.kkiri.repository.moim.MoimRepository;
import com.lets.kkiri.repository.noti.NotiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotiService {
    private final NotiLogRepository notiLogRepository;
    private final MoimRepository moimRepository;
    private final MoimRepositorySupport moimRepositorySupport;
    private final MemberRepository memberRepository;
    private final MemberDeviceRepositorySupport memberDeviceRepositorySupport;
    private final FcmService fcmService;
    private final MessageRoomService messageRoomService;
    private final MessageService messageService;

    public void sendPressNoti(String senderKakaoId, String receiverKakaoId, Long chatRoomId) {
        Member recvMember = memberRepository.findByKakaoId(receiverKakaoId).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Member sender = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
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
                            .body(String.format("@{}님이 재촉 중입니다.", sender.getNickname()))
                            .sender(sender)
                            .build()
            );

            //재촉 메세지 채팅방에 전송
            MessageDto dto = MessageDto.builder()
                    .moimId(chatRoomId)
//                    .message(senderKakaoId+"님이 " + receiverKakaoId+"님을 재촉 중입니다.")
                    .message(String.format("@{}님이 @{}님을 재촉 중입니다.}", sender.getNickname(), recvMember.getNickname()))
                    .build();
            messageRoomService.sendMessage(MoimSessionReq.MoimSessionType.URGENT, dto);

        } catch (IOException e) {
            log.error("FCM ERROR");
        }

        successLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity("hurry", senderKakaoId, receiverKakaoId, chatRoomId));
        });
    }

    public void sendHelpNoti(String senderKakaoId, Long chatRoomId) {
        Moim targetMoim = moimRepository.findById(chatRoomId).orElseThrow(
                () -> new IllegalArgumentException("해당 모임이 존재하지 않습니다.")
        );

        Member member = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")
        );

        // 보내는 사람을 제외한 모임원들의 토큰들을 불러옴.
        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMoimId(member.getId(), targetMoim.getId().toString());
        if (tokenList.size() == 0) throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

        List<NotiLogDto> successLogList = new ArrayList<>();
        try {
            successLogList = fcmService.sendMessageToToken(
                    FcmMessageDto.builder()
                            .tokenList(tokenList)
                            .channelId("sos")
                            .title(String.format("@{}님이 도움을 요청했어요!", member.getNickname()))
                            .body("길을 헤매는 친구에게 길 안내를 보내주세요!")
                            .sender(member)
                            .build()
            );
        } catch (IOException e) {
            log.error("FCM ERROR");
        }

        successLogList.forEach((log) -> {
            notiLogRepository.save(log.toEntity("sos", senderKakaoId, senderKakaoId, chatRoomId));
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
            notiLogRepository.save(log.toEntity("path", senderKakaoId, routeGuideReq.getReceiverKakaoId(), routeGuideReq.getChatRoomId()));
        });
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void sendCommingMoim(){
        String eventTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now().plusHours(1));
        List<Moim> moims = moimRepositorySupport.findMoimsByMeetingAt(eventTime);

        List<String> tokenList = new ArrayList<>();
        List<NotiLogDto> successLogList = new ArrayList<>();

        for(int i = 0; i < moims.size(); i ++ ){
            tokenList.addAll(memberDeviceRepositorySupport.findTokenListByMoimId(0l, moims.get(i).getId().toString()));

            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("comming")
                                .title("모임 임박 알림")
                                .body(moims.get(i).getName() + "모임이 임박했습니다.")
                                .moim(moims.get(i))
                                .build()
                );
            } catch (IOException e) {
                log.error("FCM ERROR");
            }

            successLogList.forEach((log) -> {
                notiLogRepository.save(log.toEntity("comming", "000000", log.getReceiver(), log.getMoimId()));
            });
        }
    }
}
