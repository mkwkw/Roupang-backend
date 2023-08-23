package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.dto.OAuthToken.OAuthToken;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.service.OAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@Controller
@RequestMapping("/login")
public class OAuth2Controller {

    @Autowired
    private  OAuth2UserService oAuth2UserService;

        @GetMapping
        public String showLoginPage() {
            return "login";
        }


        @GetMapping("/oauth2/code/kakao")
        public ResponseEntity<String> kakaoCallback(@RequestParam String code , HttpServletResponse response){

            OAuthToken oAuthToken =  oAuth2UserService.createOAuthToken(code);

            RestTemplate rt2 = new RestTemplate();

            //HttpHeader 오브젝트 생성
            HttpHeaders headers2 = new HttpHeaders();
            headers2.add("Authorization","Bearer " + oAuthToken.getAccess_token());
            headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

            boolean isNewUser = oAuth2UserService.checkUser(headers2,rt2);
            if (isNewUser){
                Member member = oAuth2UserService.createOAuthMember(headers2,rt2);
                oAuth2UserService.OAuthLoginMember(member,response);
                return ResponseEntity.ok("회원가입 및 로그인 성공");
            }else {
            Member member = oAuth2UserService.returnMember(headers2,rt2);
            oAuth2UserService.OAuthLoginMember(member,response);
                return ResponseEntity.ok("로그인 성공");
            }
        }
    }
