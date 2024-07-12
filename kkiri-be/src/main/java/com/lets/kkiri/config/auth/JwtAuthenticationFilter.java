package com.lets.kkiri.config.auth;

import com.lets.kkiri.common.util.JwtTokenUtil;
import com.lets.kkiri.config.ResponseBodyWriteUtil;
import com.lets.kkiri.config.auth.MemberDetails;
import com.lets.kkiri.entity.Member;
import com.lets.kkiri.service.MemberService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

/**
 * 요청 헤더에 jwt 토큰이 있는 경우, 토큰 검증 및 인증 처리 로직 정의.
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {
    private MemberService memberService;
    private RedisTemplate redisTemplate;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, MemberService memberService, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.memberService = memberService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Read the Authorization header, where the JWT Token should be
        String token = request.getHeader(JwtTokenUtil.HEADER_STRING);

        /*
         * Spring Security에서 권한 획득하려면
         * 프론트로부터 Bearer [refresh_token]
         * 으로 값을 얻어와야 함.
         */
        // header가 null이거나, 'Bearer '로 시작하지 않는다면 예외 처리.
        if (token == null || !token.startsWith(JwtTokenUtil.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        /*
        [토큰 블랙리스트 확인] 시작
            header => 'Bearer [accessToken]'으로 구성되어 있다.
            따라서 header.substr(7) => accessToken 값이 된다

            해당 토큰값으로 된 key 레코드가 레디스에 있다면 사용하지 못하게 필터링
         */
        String kakaoId = JwtTokenUtil.getIdentifier(token);
        String isLogout = (String) redisTemplate.opsForValue().get(kakaoId);

        if ("logout".equals(isLogout)) {
            filterChain.doFilter(request, response);
            return;
        } // [토큰 블랙리스트 확인] 끝

        try {
            // If header is present, try grab user principal from database and perform authorization
            Authentication authentication = getAuthentication(request);
            // jwt 토큰으로 부터 획득한 인증 정보(authentication) 설정.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ex) {
            ResponseBodyWriteUtil.sendError(request, response, ex);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Transactional(readOnly = true)
    public Authentication getAuthentication(HttpServletRequest request) throws Exception {
        String token = request.getHeader(JwtTokenUtil.HEADER_STRING);
        // 요청 헤더에 Authorization 키값에 jwt 토큰이 포함된 경우에만, 토큰 검증 및 인증 처리 로직 실행.
        if (token != null) {
            // parse the token and validate it (decode)
            String kakaoId = JwtTokenUtil.getIdentifier(token);

            // Search in the DB if we find the user by token subject (username)
            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
            if (kakaoId != null) {
                // jwt 토큰에 포함된 계정 정보(userId) 통해 실제 디비에 해당 정보의 계정이 있는지 조회.
                Member member = memberService.getMemberByKakaoId(kakaoId);
                // 식별된 정상 유저인 경우, 요청 context 내에서 참조 가능한 인증 정보(jwtAuthentication) 생성.
                MemberDetails userDetails = new MemberDetails(member);
                UsernamePasswordAuthenticationToken jwtAuthentication = new UsernamePasswordAuthenticationToken(kakaoId,
                        null, userDetails.getAuthorities());
                jwtAuthentication.setDetails(userDetails);
                return jwtAuthentication;
            }
            return null;
        }
        return null;
    }
}