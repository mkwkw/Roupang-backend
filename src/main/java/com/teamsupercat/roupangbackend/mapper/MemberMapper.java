package com.teamsupercat.roupangbackend.mapper;

import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    private final PasswordEncoder passwordEncoder;
    public Member makeMemberEntity(SignupRequestDto signupRequestDto){
        Member member = Member.builder()
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .userPassword(passwordEncoder.encode(signupRequestDto.getPassword()))
                .phoneNumber(signupRequestDto.getPhoneNumber())
                .address(signupRequestDto.getAddress())
                .memberImg(signupRequestDto.getMemberImg())
                .isDeleted(false)
                .userPoint(0L)
                .build();

        return member;
    }
}
