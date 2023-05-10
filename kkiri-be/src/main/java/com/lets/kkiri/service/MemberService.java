package com.lets.kkiri.service;

import com.lets.kkiri.dto.member.KakaoUserPostDto;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    public Member getMemberByKakaoId(String kakaoId) {
        Member member = memberRepository.findByKakaoId(kakaoId).orElseThrow(
                () -> {
                    log.error("MemberService.getMemberByEmail: member not found");
                    return new IllegalArgumentException("MemberService.getMemberByEmail: member not found");
                }
        );
        return member;
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
}
