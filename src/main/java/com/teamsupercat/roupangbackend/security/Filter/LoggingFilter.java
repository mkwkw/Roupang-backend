package com.teamsupercat.roupangbackend.security.Filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String method = request.getMethod();
        String url = String.valueOf(request.getRequestURL());

        log.info(method + url + " 요청이 들어왔습니다");

        filterChain.doFilter(request,response);

        log.info(method + url +" 가 상태 " + response.getStatus() + " 로 응답이 나갑니다.");

    }
}