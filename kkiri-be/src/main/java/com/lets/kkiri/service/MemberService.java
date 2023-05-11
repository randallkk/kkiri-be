package com.lets.kkiri.service;

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
    public Member getMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> {
                    log.error("MemberService.getMemberByEmail: member not found");
                    return new IllegalArgumentException("MemberService.getMemberByEmail: member not found");
                }
        );
        return member;
    }
}
