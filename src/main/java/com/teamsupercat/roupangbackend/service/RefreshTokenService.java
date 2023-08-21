package com.teamsupercat.roupangbackend.service;


import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.RefreshToken;
import com.teamsupercat.roupangbackend.repository.RefreshTokenRepository;
import com.teamsupercat.roupangbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final JwtTokenProvider jwtTokenProvider;

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseDto<?> RefreshAccessToken(HttpServletRequest request, HttpServletResponse response){

        String jwtToken = jwtTokenProvider.resolveToken(request);

        Integer memberId = jwtTokenProvider.getMemberIdx(jwtToken);

        RefreshToken token = refreshTokenRepository.findByMemberIdx(memberId).orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND_TOKEN));
        //영속성 콘테스트에 저장

        Member member = jwtTokenProvider.getMember(jwtToken);

        String accessToken  =  jwtTokenProvider.createAccessToken(member);

        String refreshToken = jwtTokenProvider.createRefreshToken(member);

        token.updateToken(refreshToken);

        response.setHeader("Authorization",accessToken);

        return ResponseDto.success("토큰 재발급 성공");
    }
}
