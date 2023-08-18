package com.teamsupercat.roupangbackend.config;

import com.teamsupercat.roupangbackend.security.CustomAuthenticationEntryPoint;
import com.teamsupercat.roupangbackend.security.CustomerAccessDeniedHandler;
import com.teamsupercat.roupangbackend.security.Filter.JwtAuthenticationFilter;
import com.teamsupercat.roupangbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.headers().frameOptions().sameOrigin()
                .and().formLogin().disable()
                .csrf().disable()
                .httpBasic().disable()
                .rememberMe().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/resources/static/**","/api/v1/**").permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomerAccessDeniedHandler())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*","http://localhost:3000")); // 모든 도메인을 허용
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP 메서드를 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); //모든 HTTP 헤더를 허용
        configuration.setAllowCredentials(true); // 자격 증명 정보를 요청

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 정책 적용
        return source;
    }
}
