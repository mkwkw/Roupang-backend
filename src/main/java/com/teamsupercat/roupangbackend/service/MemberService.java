package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.mapper.MemberMapper;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Transactional
    public ResponseDto<?> createMember(@RequestBody SignupRequestDto signupRequestDto) {

        boolean emailCheck = memberRepository.existsByEmail(signupRequestDto.getEmail());
        boolean nickNameCheck = memberRepository.existsByNickname(signupRequestDto.getNickname());
        boolean phoneNumberCheck = memberRepository.existsByPhoneNumber(signupRequestDto.getPhoneNumber());

        if (emailCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_EMAIL);
        if (nickNameCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_NICKNAME);
        if (phoneNumberCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_PHONENUMBER);

        Member member = memberMapper.makeMemberEntity(signupRequestDto);

        memberRepository.save(member);

        return ResponseDto.success("회원가입에 성공하였습니다.");
    }

    @Transactional
//    public ResponseDto<?> deleteMember(UserDetails userDetails) {
    public ResponseDto<?> deleteMember() {
        Member member = memberRepository.findByEmail("superCat1@gmail.com")
                .orElseThrow(() -> new CustomException(ErrorCode.SIGNOUT_NOT_FOUND_EMAIL));

        member.deleteMember();

        return ResponseDto.success("회원탈퇴에 성공하였습니다.");
    }
}
