package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.member.DuplicateCheckDto;
import com.teamsupercat.roupangbackend.dto.member.LoginRequestDto;
import com.teamsupercat.roupangbackend.dto.member.SignupRequestDto;
import com.teamsupercat.roupangbackend.entity.LoginAttempt;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.RefreshToken;
import com.teamsupercat.roupangbackend.mapper.MemberMapper;
import com.teamsupercat.roupangbackend.repository.LoginFailRepository;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.RefreshTokenRepository;
import com.teamsupercat.roupangbackend.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@RequiredArgsConstructor
@Service
public class MemberService {
    final long plusMin = 60 * 5;
    private final MemberMapper memberMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final LoginFailRepository loginFailRepository;

    @Transactional
    public ResponseDto<?> createMember(@RequestBody SignupRequestDto signupRequestDto) {

        boolean emailCheck = memberRepository.existsByEmail(signupRequestDto.getEmail());
        boolean nickNameCheck = memberRepository.existsByNickname(signupRequestDto.getNickname());
        boolean phoneNumberCheck = memberRepository.existsByPhoneNumber(signupRequestDto.getPhoneNumber());

        if (emailCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_EMAIL);
        if (nickNameCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_NICKNAME);
        if (phoneNumberCheck) throw new CustomException(ErrorCode.SIGNUP_DUPLICATE_PHONE_NUMBER);

        signupRequestDto.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        Member member = memberMapper.MemberFromSignupDto(signupRequestDto);

        memberRepository.save(member);

        return ResponseDto.success("회원가입에 성공하였습니다.");
    }

    @Transactional
    public ResponseDto<?> deleteMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.SIGN_OUT_NOT_FOUND_EMAIL));

        member.deleteMember();

        return ResponseDto.success("회원탈퇴에 성공하였습니다.");
    }

    @Transactional(noRollbackFor = {CustomException.class})
    public ResponseDto<?> loginMember(LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response){
        String domain = request.getRemoteAddr();
        Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGIN_NOT_FOUND_EMAIL));

        LoginAttempt loginCheck = loginFailRepository.findByDomainAndEmail(domain,loginRequestDto.getEmail())
                .orElseGet(() -> memberMapper.makeLoginFail(loginRequestDto.getEmail(), domain));

        boolean allowedTime = checkAllowedTime(loginCheck);

        if (!allowedTime) {
            throw new CustomException(ErrorCode.LOGIN_BEFORE_ALLOWED_LOGIN_TIME);
        }

        try {
            if(passwordEncoder.matches(loginRequestDto.getPassword(),member.getUserPassword())){
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

                loginCheck.init();
            }else {
                throw new CustomException(ErrorCode.LOGIN_NOT_MATCH_PASSWORD);
            }
            return ResponseDto.success("로그인에 성공하였습니다.");
        }finally {
            loginCheck.setTrial(loginCheck.getTrial()+1);
            Integer count = loginCheck.getTrial();
            if(count%5==0){
                loginCheck.setAllowedLoginTime(Instant.now().plusSeconds(plusMin*(count/5)));
            }
            loginFailRepository.save(loginCheck);
        }
    }
    @Transactional
    public ResponseDto<?> logoutMember(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_NOT_FOUND_EMAIL));

        RefreshToken refreshToken = refreshTokenRepository.findByMemberIdx(member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.LOGOUT_NOT_FOUND_TOKEN));

        refreshToken.deleteToken();

        return ResponseDto.success("로그아웃에 성공하였습니다.");
    }

    public ResponseDto<?> duplicateCheck(DuplicateCheckDto checkDto) {
        String eamil = checkDto.getEmail();
        String nickname = checkDto.getNickname();

        if(eamil != null){
            boolean emailCheck = memberRepository.existsByEmail(eamil);
            if(emailCheck) throw new CustomException(ErrorCode.SIGNUP_CHECK_EMAIL);
            else return ResponseDto.success("사용가능한 이메일입니다.");

        } else if (nickname != null) {
            boolean nicknameCheck = memberRepository.existsByNickname(nickname);
            if(nicknameCheck) throw new CustomException(ErrorCode.SIGNUP_CHECK_NICKNAME);
            else return ResponseDto.success("사용가능한 닉네임입니다.");

        }else{
            throw new CustomException(ErrorCode.SIGNUP_CHECK_FAIL);
        }
    }

    public Boolean checkAllowedTime (LoginAttempt loginAttempt)  {
        if(loginAttempt == null)return true;
        Instant now = Instant.now();
        now = now.plusMillis(1L);
        return now.isAfter(loginAttempt.getAllowedLoginTime());
    }

    @Transactional
    public void save (LoginAttempt loginAttempt) {
       loginFailRepository.save(loginAttempt);
    }
}
