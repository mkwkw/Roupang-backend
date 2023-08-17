package com.teamsupercat.roupangbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    SIGNUP_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이메일입니다."),
    SIGNUP_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임입니다."),
    SIGNUP_DUPLICATE_PHONENUMBER(HttpStatus.BAD_REQUEST,"중복된 전화번호입니다."),
    SIGNOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"토큰에서 이메일을 찾을수 없습니다."),
    LOGIN_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"등록되어 있지 않은 이메일 입니다."),
    LOGIN_NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"등록된 회원의 정보와 일치하지 않습니다."),
    LOGOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"등록되어 있지 않은 이메일 입니다."),
    LOGOUT_NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST,"맴버에 해당하는 토큰을 찾을수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMsg;

}