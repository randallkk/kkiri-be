package com.lets.kkiri.service;

import com.lets.kkiri.dto.auth.OAuth2UserInfo;
import com.lets.kkiri.dto.auth.OAuthAttributes;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.entity.Role;
import com.lets.kkiri.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService implements OAuth2UserService {

    // private final OAuth2UserInfo oAuth2UserInfo;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // OAuth2 서비스 제공자 구분
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // OAuth2 계정 정보 가져오기
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuthAttributes oAuthAttributes = OAuthAttributes.of(provider, userNameAttributeName, attributes);
        log.info("oAuthAttributes: {}", oAuthAttributes);

        Member member = saveOrUpdate(provider, oAuthAttributes.getOAuth2UserInfo());
        log.debug("member: {}", member.toString());

        // TODO CustonOAuth2User으로 바꿔서 쿼리 한번 덜 날리게 리팩토링
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().toString())),
                oAuthAttributes.getOAuth2UserInfo().getAttributes(),
                oAuthAttributes.getUserNameAttributeName()
        );
    }

    @Transactional
    public Member saveOrUpdate(String provider, OAuth2UserInfo oAuth2UserInfo) {

        Member oAuthMember = Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .role(Role.ROLE_MEMBER)
                .build();

        Optional<Member> findMember =  memberRepository.findByEmail(oAuthMember.getEmail());
        log.debug("oAuthMember.getIdentifier: {}", oAuthMember.getEmail());
        Member member;
        if (findMember.isPresent()) {
            log.info("findMember.isPresent: 이미 회원가입한 적 있는 회원입니다.");
            // TODO: 회원가입한 적 있는 회원이면 소셜 로그인 방식 확인 후 소셜 로그인 방식이 다르면 예외처리 (이미 다른 소셜 로그인 방식으로 가입한 회원입니다.)
            throw new RuntimeException("이미 다른 소셜 로그인 방식으로 가입한 회원입니다.");
        } else {
            log.info("findMember.isNOTPresent: 회원가입한 적 없는 회원입니다.");
            member = memberRepository.save(oAuthMember);
        }
        log.debug("member: {}", member.toString());
        return member;
    }

    // @Override
    // public Member getMember(String token) {
    //     String identifier = JwtTokenUtil.getIdentifier(token);
    //     Optional<Member> findMember = memberRepository.findByEmail(identifier);
    //     if (findMember.isPresent()) {
    //         return findMember.get();
    //     } else{
    //         throw new MemberNotFoundException();
    //     }
    // }


}
