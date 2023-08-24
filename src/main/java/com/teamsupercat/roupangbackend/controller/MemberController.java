package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.member.DuplicateCheckDto;
import com.teamsupercat.roupangbackend.dto.member.LoginRequestDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/member")
@Api(tags = "맴버 API(회원가입,로그인)")
public class MemberController {

    private final MemberService memberService;

    @ApiOperation(value = "회원가입")
    @PostMapping(value = "/register")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.createMember(signupRequestDto);

    }
    @ApiOperation(value = "회원탈퇴")
    @PatchMapping(value = "/delete")
    public ResponseDto<?> signOut(@AuthenticationPrincipal CustomUserDetail userDetails) {
        String memberEmail = userDetails.getUsername();
        return memberService.deleteMember(memberEmail);
    }

    @ApiOperation(value = "로그인")
    @PostMapping(value = "/login")
    public ResponseDto<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response){
        return memberService.loginMember(loginRequestDto,request,response);
    }

    @ApiOperation(value = "로그아웃")
    @PatchMapping(value = "/logout")
    public ResponseDto<?> logout(@AuthenticationPrincipal CustomUserDetail userDetails) {
        String memberEmail = userDetails.getUsername();
        return memberService.logoutMember(memberEmail);
    }
    @ApiOperation(value = "닉네임,이메일 중복체크")
    @PostMapping(value = "check")
    public ResponseDto<?> duplicateCheck(@RequestBody DuplicateCheckDto checkDto) {
        return memberService.duplicateCheck(checkDto);
    }
}
