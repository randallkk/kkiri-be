package com.lets.kkiri.service;

import com.lets.kkiri.common.exception.ErrorCode;
import com.lets.kkiri.common.exception.KkiriException;
import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.MemberDevice;
import com.lets.kkiri.repository.member.MemberDeviceRepository;
import com.lets.kkiri.repository.member.MemberDeviceRepositorySupport;
import com.lets.kkiri.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberDeviceRepository memberDeviceRepository;
    public Member getMemberByKakaoId(String kakaoId) {
        return memberRepository.findByKakaoId(kakaoId).orElseThrow(() -> {
            throw new KkiriException(ErrorCode.MEMBER_NOT_FOUND);
        });
    }

    public Member getMemberByKakaoUserInfo(KakaoUserPostDto kakaoUserInfo) {
        Optional<Member> member = memberRepository.findByKakaoId(kakaoUserInfo.getKakaoId());
        return member.orElseGet(() -> {
            memberRepository.save(kakaoUserInfo.toEntity());
            return kakaoUserInfo.toEntity();
        });
    }

    public void createUser(Member member) {
        memberRepository.save(member);
    }

    public List<String> getDeviceTokensByMemberId(Long memberId) {
        return memberDeviceRepository.findByMemberId(memberId).stream()
                .map(MemberDevice::getToken)
                .collect(Collectors.toList());
    }

    public void addDeviceToken(String kakaoId, String deviceToken) {
        Member member = getMemberByKakaoId(kakaoId);
        memberDeviceRepository.save(
                MemberDevice.builder()
                        .token(deviceToken)
                        .member(member)
                        .build());
    }
}
