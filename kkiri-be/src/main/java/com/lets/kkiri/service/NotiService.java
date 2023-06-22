package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.chatting.MessageDto;
import com.lets.kkiri.dto.fcm.FcmMessageDto;
import com.lets.kkiri.dto.moim.MoimSessionReq;
import com.lets.kkiri.dto.noti.NotiLogDto;
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

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
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
        if (tokenList.size() > 0) {

            List<NotiLogDto> successLogList = new ArrayList<>();
            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("hurry")
                                .title("재촉 알림")
                                .body(sender.getNickname() + "님이 재촉 중입니다.")
                                .sender(sender)
                                .build()
                );

                //재촉 메세지 채팅방에 전송
                MessageDto dto = MessageDto.builder()
                        .moimId(chatRoomId)
                        .message(sender.getNickname() + "님이 @" + recvMember.getNickname() + "님을 재촉 중입니다.")
                        .build();
                messageRoomService.sendMessage(MoimSessionReq.MoimSessionType.URGENT, dto);

            } catch (IOException e) {
                log.error("FCM ERROR");
            }

            successLogList.forEach((log) -> {
                notiLogRepository.save(log.toEntity("hurry", senderKakaoId, receiverKakaoId, chatRoomId));
            });
        }
    }

    public void sendHelpNoti(String senderKakaoId, Long moimId) {
        Moim targetMoim = moimRepository.findById(moimId).orElseThrow(
                () -> new KkiriException(ErrorCode.MOIM_NOT_FOUND)
        );

        Member member = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 보내는 사람을 제외한 모임원들의 토큰들을 불러옴.
        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMoimId(member.getId(), targetMoim.getId().toString());
        if (tokenList.size() > 0) {

            List<NotiLogDto> successLogList = new ArrayList<>();
            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("sos")
                                .title("@" + member.getNickname() + "님이 도움을 요청했어요!")
                                .body("길을 헤매는 친구에게 길 안내를 보내주세요!")
                                .sender(member)
                                .build()
                );
            } catch (IOException e) {
                log.error("FCM ERROR");
            }

            successLogList.forEach((log) -> {
                notiLogRepository.save(log.toEntity("sos", senderKakaoId, senderKakaoId, moimId));
            });
        }
    }

    public void sendRoute(String senderKakaoId, RouteGuideNotiReq routeGuideReq) {
        Member recvMember = memberRepository.findByKakaoId(routeGuideReq.getReceiverKakaoId()).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        Member sender = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMemberId(recvMember.getId());
        if (tokenList.size() > 0) {

            List<NotiLogDto> successLogList = new ArrayList<>();
            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("path")
                                .title("길 안내 알림")
                                .sender(sender)
                                .body("@" + sender.getNickname() + "님이 길 안내 중입니다.")
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
    }

    @Scheduled(cron = "0 */5 * * * *")
    public void sendCommingMoim() {
        String eventTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now().plusHours(1));
        List<Moim> moims = moimRepositorySupport.findMoimsByMeetingAt(eventTime);

        List<String> tokenList = new ArrayList<>();
        List<NotiLogDto> successLogList = new ArrayList<>();

        for (int i = 0; i < moims.size(); i++) {
            tokenList.addAll(memberDeviceRepositorySupport.findTokenListByMoimId(0l, moims.get(i).getId().toString()));

            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("comming")
                                .title("모임 임박 알림")
                                .body("실시간으로 친구들의 위치를 확인해 보세요!")
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

    //시연용
    public void sendImminentNoti(Long moimId) {
        Moim moim = moimRepository.findById(moimId).orElseThrow(
                () -> new KkiriException(ErrorCode.MOIM_NOT_FOUND)
        );

        List<String> tokenList = new ArrayList<>();
        List<NotiLogDto> successLogList = new ArrayList<>();

        tokenList.addAll(memberDeviceRepositorySupport.findTokenListByMoimId(0l, moimId.toString()));
        try {
            successLogList = fcmService.sendMessageToToken(
                FcmMessageDto.builder()
                    .tokenList(tokenList)
                    .channelId("comming")
                    .title("모임 임박 알림")
                    .body("실시간으로 친구들의 위치를 확인해 보세요!")
                    .moim(moim)
                    .build()
            );
        } catch (IOException e) {
            log.error("FCM ERROR");
        }
    }

    public void sendPaymentNoti(String senderKakaoId, Long moimId) {
        Moim targetMoim = moimRepository.findById(moimId).orElseThrow(
                () -> new KkiriException(ErrorCode.MOIM_NOT_FOUND)
        );

        Member member = memberRepository.findByKakaoId(senderKakaoId).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 보내는 사람을 제외한 모임원들의 토큰들을 불러옴.
        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMoimId(member.getId(), targetMoim.getId().toString());
        if (tokenList.size() > 0) {

            List<NotiLogDto> successLogList = new ArrayList<>();
            try {
                successLogList = fcmService.sendMessageToToken(
                        FcmMessageDto.builder()
                                .tokenList(tokenList)
                                .channelId("payment")
                                .title("정산 요청 알림")
                                .body("정산을 완료해주세요 !")
                                .moim(targetMoim)
                                .sender(member)
                                .build()
                );
            } catch (IOException e) {
                log.error("FCM ERROR");
            }

            successLogList.forEach((log) -> {
                notiLogRepository.save(log.toEntity("payment", senderKakaoId, senderKakaoId, moimId));
            });
        }
    }
}
