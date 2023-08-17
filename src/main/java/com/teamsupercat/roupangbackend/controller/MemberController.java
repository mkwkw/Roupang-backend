package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class MemberController {

    private final MemberService memberService;
    @PostMapping(value = "/register")
    public ResponseDto<?> signUp(@RequestBody SignupRequestDto signupRequestDto) {
        return memberService.createMember(signupRequestDto);

    }

    @PatchMapping(value = "/deleteuser")
//    public ResponseDto<?> signOut(@AuthenticationPrincipal UserDetails userDetails) {
//        return memberService.deleteMember(userDetails);
    public ResponseDto<?> signOut() {
        return memberService.deleteMember();
    }
}
