package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.fcm.FcmMessageDto;
import com.lets.kkiri.dto.noti.MessageNotiDto;
import com.lets.kkiri.dto.noti.NotiLogDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.Moim;
import com.lets.kkiri.repository.member.MemberDeviceRepositorySupport;
import com.lets.kkiri.repository.member.MemberRepository;
import com.lets.kkiri.repository.moim.MoimRepository;
import com.lets.kkiri.repository.noti.NotiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatNotiService {
    private final NotiLogRepository notiLogRepository;
    private final MoimRepository moimRepository;
    private final MemberRepository memberRepository;
    private final MemberDeviceRepositorySupport memberDeviceRepositorySupport;
    private final FcmService fcmService;
    public void sendChatNoti(MessageNotiDto message) {
        Moim targetMoim = moimRepository.findById(message.getMoimId()).orElseThrow(
                () -> new KkiriException(ErrorCode.MOIM_NOT_FOUND)
        );

        Member sender = memberRepository.findByKakaoId(message.getSenderKakaoId()).orElseThrow(
                () -> new KkiriException(ErrorCode.MEMBER_NOT_FOUND)
        );

        // 보내는 사람을 제외한 모임원들의 토큰들을 불러옴.
        List<String> tokenList = memberDeviceRepositorySupport.findTokenListByMoimId(sender.getId(), targetMoim.getId().toString());
        if (tokenList.size() == 0) throw new IllegalArgumentException("토큰이 존재하지 않습니다.");

        List<NotiLogDto> successLogList = new ArrayList<>();
        try {
            successLogList = fcmService.sendMessageToToken(
                    FcmMessageDto.builder()
                            .tokenList(tokenList)
                            .channelId("chat")
                            .title("채팅 알림")
                            .body(sender.getNickname() + "님이 채팅을 보냈습니다.")
                            .moimName(targetMoim.getName())
                            .time(message.getTime())
                            .senderNickname(sender.getNickname())
                            .message(message.getMessage())
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
