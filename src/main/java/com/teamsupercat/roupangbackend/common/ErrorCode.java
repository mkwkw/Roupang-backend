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
    SIGNOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"토큰에서 이메일을 찾을수 없습니다.");

    private final HttpStatus httpStatus;
    private final String errorMsg;

}