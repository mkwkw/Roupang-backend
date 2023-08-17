package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.LoginRequesrDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.RefreshToken;
import com.teamsupercat.roupangbackend.mapper.MemberMapper;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.RefreshTokenRepository;
import com.teamsupercat.roupangbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public ResponseDto<?> createMember(@RequestBody SignupRequestDto signupRequestDto) {

        boolean emailCheck = memberRepository.existsByEmail(signupRequestDto.getEmail());
        boolean nickNameCheck = memberRepository.existsByNickname(signupRequestDto.getNickname());
        boolean phoneNumberCheck = memberRepository.existsByPhoneNumber(signupRequestDto.getPhoneNumber());

        if (emailCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_EMAIL);
        if (nickNameCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_NICKNAME);
        if (phoneNumberCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_PHONENUMBER);

        signupRequestDto.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        Member member = memberMapper.MemberFromSignupDto(signupRequestDto);

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

    @Transactional
    public ResponseDto<?> loginMember(LoginRequesrDto loginRequesrDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(loginRequesrDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_NOT_FOUND_EMAIL));

        if(passwordEncoder.matches(loginRequesrDto.getPassword(),member.getUserPassword())){
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

            response.setHeader("Authorization","Bearer "+accessToken);

            return ResponseDto.success("로그인에 성공하였습니다.");
        }else {
            throw new CustomException(ErrorCode.LOGIN_NOT_MATCH_PASSWORD);
        }
    }
    @Transactional
    public ResponseDto<?> logoutMember() {
        Member member = memberRepository.findByEmail("superCat1@gmail.com")
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_NOT_FOUND_EMAIL));

        RefreshToken refreshToken = refreshTokenRepository.findByMemberIdx(member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_NOT_FOUND_TOKEN));

        refreshToken.deleteToken();

        return ResponseDto.success("로그아웃에 성공하였습니다.");
    }
}
