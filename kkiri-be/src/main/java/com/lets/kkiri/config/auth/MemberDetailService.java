package com.lets.kkiri.config.auth;

import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 현재 액세스 토큰으로 부터 인증된 유저의 상세정보(활성화 여부, 만료, 롤 등) 관련 서비스 정의.
 */
@Component
public class MemberDetailService implements UserDetailsService{
    @Autowired
    MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberService.getMemberByEmail(email);
        if(member != null) {
            MemberDetails userDetails = new MemberDetails(member);
            return userDetails;
        }
        return null;
    }
}