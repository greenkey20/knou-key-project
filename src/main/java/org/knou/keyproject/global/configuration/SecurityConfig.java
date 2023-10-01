package org.knou.keyproject.global.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 2023.10.1(일) 22H20
// 책 "스프링부트 쇼핑몰 프로젝트 with JPA" extends WebSecurityConfigurerAdapter = deprecated
// Spring Security doc = https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
@RequiredArgsConstructor
@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userService;

    /**
     * Spring Security 기능 비활성화
     *
     * @return
     */
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .requestMatchers("/static/**");
//    }

    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
//            .requestMatchers("/login", "/mainPage.cm", "/loginPage.me", "/login.me", "/enroll.me", "/newMemberInsert.me", "/logout.me").permitAll()
                .anyRequest().permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/loginPage.me")
                .defaultSuccessUrl("/mainPage.cm")
                .and()
                .logout()
                .logoutSuccessUrl("/mainPage.cm")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable();

        return http.build();
    }

    /**
     * 인증 관리자 관련 설정
     * @param http
     * @param bCryptPasswordEncoder
     * @param userDetailsService
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       BCryptPasswordEncoder bCryptPasswordEncoder,
                                                       UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    /**
     * 패스워드 인코더로 사용할 빈 등록
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
