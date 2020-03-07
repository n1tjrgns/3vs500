package com.service.applehip.config.auth;

import com.service.applehip.domain.users.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // SpringSecurity 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {  // 스프링 시큐리티 설정 Config 클래스

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /*
        *  headers().frameOptions().disable() - h2 - console을 사용하기 위해 disable
        *  authorizeRequests() - URL 별 권한 관리를 설정하는 옵션의 시작, authorizeRequests 가 있어야 뒤에 antMatchers 옵션 사용 가능
        *  antMatchers() - URL, HTTP 메소드별 관리가 가능
        *                - "/"등 지정된 URL들은 permitAll() 옵션을 통해 전체 열람 권한을 부여
        *                - /3vs500/v1/users").hasRole(Role.USER.name()) 해당 url을 가지는 API는 USER권한을 가진 사람만 열람 가능
        *  anyRequest() - 설정된 값들 이외 나머지 URL들을 나타냄
        *  authenticated() - 나머지 URL들은 모두 인증된 사용자들에게만 허용 (= 인증된 사용자.equal( 로그인한 사용자))
        *  logoutSuccessUrl("/") - 로그아웃 성공시 해당 URL로 이동
        *  oauth2Login() - OAuth2 로그인 기능에 대한 여러 설정의 진입점
        *  userInfoEndpoint - OAuth2 로그인 성공 이후 사용자 정보를 가져올 때 설정 담당
        *  userService - 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록
        *              - 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능 명시
        * */


        http
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    .antMatchers("/","/css/**","/images/**","/js/**","/h2-console/**").permitAll()
                    .antMatchers("/3vs500/v1/users").hasRole(Role.USER.name())
                    .anyRequest().authenticated()
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                            .userService(customOAuth2UserService);

    }
}
