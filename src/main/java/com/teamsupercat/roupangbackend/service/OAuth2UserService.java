package com.teamsupercat.roupangbackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.OAuthToken.KakaoProfile;
import com.teamsupercat.roupangbackend.dto.OAuthToken.KakaoUriDto;
import com.teamsupercat.roupangbackend.dto.OAuthToken.OAuthToken;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.RefreshToken;
import com.teamsupercat.roupangbackend.mapper.MemberMapper;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.RefreshTokenRepository;
import com.teamsupercat.roupangbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserService {

    private final MemberMapper memberMapper;

    private final RefreshTokenRepository refreshTokenRepository;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final KakaoUriDto kakaoUriDto;



    @Transactional
    public Member createOAuthMember(HttpHeaders headers2, RestTemplate rt2) {
        //Header와 Bod를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
                new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        // 필요한거 닉네임, 이메일, 주소, 이미지, 휴대폰 전화번호
        UUID garbagePassword = UUID.randomUUID();


        // 본 가입을 할때 중복값이 발생할 수 있음

        Member member = Member.builder()
                .nickname(kakaoProfile.kakao_account.profile.nickname)
                .memberImg(kakaoProfile.kakao_account.profile.profile_image_url)
                .address("")
                .userPoint(0L)
                .phoneNumber("")
                .userPassword(String.valueOf(garbagePassword))
                .email(kakaoProfile.kakao_account.email)
                .isDeleted(false)
                .build();




        member.setUserPassword(passwordEncoder.encode(member.getUserPassword()));

        memberRepository.save(member);

        return member;
    }
    @Transactional
    public ResponseDto<?> OAuthLoginMember(Member member, HttpServletResponse response) {

        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken(member);
        RefreshToken token;

        boolean tokenExistCheck = refreshTokenRepository.existsByMemberIdx(member.getId());
        if (tokenExistCheck){
            token = refreshTokenRepository.findByMemberIdx(member.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_NOT_FOUND_TOKEN));
            token.updateToken(refreshToken);
        }else{
            token = memberMapper.RefreshTokenFromToken(member,refreshToken);
            refreshTokenRepository.save(token);
        }

        response.setHeader("Authorization",accessToken);
        return ResponseDto.success("로그인에 성공하였습니다.");
    }



    public OAuthToken createOAuthToken(String code){



        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성

        log.info(kakaoUriDto.getClient_id());
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",kakaoUriDto.getClient_id());
        params.add("redirect_uri",kakaoUriDto.getRedirect_uri());
        params.add("code",code);
        params.add("client_secret",kakaoUriDto.getClient_secret());


        //Header와 Bod를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest =
                new HttpEntity<>(params,headers);



        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );


        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(),OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return oAuthToken;
    }

    public Boolean checkUser(HttpHeaders headers2, RestTemplate rt2) {
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
                new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        boolean emailCheck = memberRepository.existsByEmail(kakaoProfile.getKakao_account().getEmail());
        if (emailCheck) {
            return false;
        }else {
            return true;
        }
    }
    @Transactional
    public Member returnMember(HttpHeaders headers2, RestTemplate rt2) {
        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
                new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );
        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(),KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Member member = memberRepository.findByEmail(kakaoProfile.kakao_account.email)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_NOT_FOUND_EMAIL));
        return member;

    }

}

