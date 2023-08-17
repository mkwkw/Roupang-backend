package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.LoginRequesrDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping(value = "/register")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.createMember(signupRequestDto);

    }

    @PatchMapping(value = "/delete")
    public ResponseDto<?> signOut(@AuthenticationPrincipal UserDetails userDetails) {
        return memberService.deleteMember(userDetails);
    }

    @PostMapping(value = "/login")
    public ResponseDto<?> login(@RequestBody LoginRequesrDto loginRequesrDto, HttpServletResponse response) {
        return memberService.loginMember(loginRequesrDto,response);
    }

    @PatchMapping(value = "/logout")
//    public ResponseDto<?> signOut(@AuthenticationPrincipal UserDetails userDetails) {
//        return memberService.deleteMember(userDetails);
    public ResponseDto<?> logout() {
        return memberService.logoutMember();
    }
}
