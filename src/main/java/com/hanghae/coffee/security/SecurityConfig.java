package com.hanghae.coffee.security;

import com.hanghae.coffee.security.jwt.JwtAuthenticationFilter;
import com.hanghae.coffee.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CorsFilter corsFilter;

    // authenticationManager를 Bean 등록합니다.
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //로그인 인증 에러 핸들링
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    // 스웨거 적용
    @Override
    public void configure(WebSecurity web) {
        web     // 스웨거 사용 허용
            .ignoring() .mvcMatchers("/swagger-ui.html/**",
                "/configuration/**", "/swagger-resources/**",
                "/v2/api-docs","/webjars/**",
                "/webjars/springfox-swagger-ui/*.{js,css}")

            // h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
            .antMatchers("/h2-console/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // cors 정책의 설정파일 등록하는 부분
        http
            .cors()
            .and()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
            .httpBasic().disable() // rest api 만을 고려
            .csrf().disable() // csrf 보안 토큰 disable처리.
            .formLogin().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/image/**").permitAll()
            // 회원 관리 처리 API 전부를 login 없이 허용
            .antMatchers("/api/user/login/**").permitAll()
            // Get 요청 login 없이 허용
            .antMatchers(HttpMethod.GET, "/api/beans/lists","/api/beans/random","/api/beans/:bean_id").permitAll()
            .antMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
            //antMatchers로 설정한 조건 외의 어떤 요청이든 '인증'해야 한다
            .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
    }
}

