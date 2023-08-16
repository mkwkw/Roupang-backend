package com.teamsupercat.roupangbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    POST_NOTFOUND(HttpStatus.BAD_REQUEST,"찾을수 없는 게시글입니다.");

    private final HttpStatus httpStatus;
    private final String errorMsg;

}