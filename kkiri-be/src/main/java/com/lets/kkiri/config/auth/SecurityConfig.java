package com.lets.kkiri.config.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lets.kkiri.config.jwt.JwtAuthenticationFilter;
import com.lets.kkiri.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 인증이 필요없는 API 목록
    private static final String[] OPEN_API_GET = new String[]{
            "/api/auth/**",
    };

    private static final String[] OPEN_API_POST = new String[]{
            "/api/auth/login",
            "/api/auth/reissue",
            "/api/auth/verify",
            "/api/moims/payment/**",
            "/api/moims/groups",

    };

    private static final String[] ADMIN_API_POST = new String[]{
            "/api/admin/**",
    };

    @Autowired
    private MemberDetailService memberDetailService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RedisTemplate redisTemplate;

    // DAO 기반으로 Authentication Provider를 생성
    // BCrypt Password Encoder와 UserDetailService 구현체를 설정
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.memberDetailService);
        return daoAuthenticationProvider;
    }

    // DAO 기반의 Authentication Provider가 적용되도록 설정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 사용 하지않음
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), memberService, redisTemplate)) //HTTP 요청에 JWT 토큰 인증 필터를 거치도록 필터를 추가
                .authorizeRequests()
                .antMatchers("/ws/**").permitAll()
                .antMatchers(HttpMethod.GET, OPEN_API_GET).permitAll()
                .antMatchers(HttpMethod.POST, OPEN_API_POST).permitAll()
                .anyRequest().authenticated()
                .and().cors();
    }


}