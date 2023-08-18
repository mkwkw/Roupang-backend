package com.teamsupercat.roupangbackend.common.dynamicMessageException;

import com.teamsupercat.roupangbackend.common.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomMessageException extends RuntimeException {
    private final ErrorCode errorCode;
    private String dynamicMessage;// 커스텀 문자열 추가가능
    private String dynamicMessage2;

}