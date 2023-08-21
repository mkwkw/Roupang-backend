package com.teamsupercat.roupangbackend.controller;


import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.VerificationEmailDto;
import com.teamsupercat.roupangbackend.dto.member.VerificationRequestDto;
import com.teamsupercat.roupangbackend.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/verification")
public class MailController {


    private final MailService mailService;

    @PostMapping("/email")
    public ResponseDto<?> MailSend(@RequestBody VerificationEmailDto email) {
        return mailService.sendEmail(email.getEmail());
    }

    @PostMapping("/code")
    public ResponseDto<?> verifyCode(@RequestBody VerificationRequestDto verificationRequestDto) {
        String email = verificationRequestDto.getEmail();
        Integer code = verificationRequestDto.getCode();

            return mailService.verificationCode(email,code);
     }
}