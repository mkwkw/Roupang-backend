package com.teamsupercat.roupangbackend.mapper;

import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberMapper {

    public Member MemberFromSignupDto(SignupRequestDto signupRequestDto){
        Member member = Member.builder()
                .nickname(signupRequestDto.getNickname())
                .email(signupRequestDto.getEmail())
                .userPassword(signupRequestDto.getPassword())
                .phoneNumber(signupRequestDto.getPhoneNumber())
                .address(signupRequestDto.getAddress())
                .memberImg(signupRequestDto.getMemberImg())
                .isDeleted(false)
                .userPoint(0L)
                .build();
        return member;
    }

    public RefreshToken RefreshTokenFromToken(Member member,String token){
       RefreshToken refreshToken = RefreshToken.builder()
               .memberIdx(member.getId())
               .token(token)
               .isDeleted(false)
               .build();
       return refreshToken;
    }
}
