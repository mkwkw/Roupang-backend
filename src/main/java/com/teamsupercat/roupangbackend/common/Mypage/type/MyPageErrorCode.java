package com.teamsupercat.roupangbackend.common.Mypage.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MyPageErrorCode {

    INVALID_INPUT("올바르지 않은 입력입니다."),
    RESOURCE_NOT_FOUND("요청한 정보를 찾을 수 없습니다."),
    PERMISSION_DENIED("권한이 거부되었습니다."),
    INTERNAL_ERROR("내부 오류가 발생했습니다."),
    SESSION_EXPIRED("세션이 만료되었습니다. 다시 로그인하세요."),
    UNAVAILABLE_SERVICE("현재 서비스 이용이 불가능합니다. 나중에 다시 시도하세요."),
    REQUIRED_FIELD_MISSING("필수정보가 누락되었습니다."),
    EMAIL_ALREADY_EXISTS("이미 등록된 이메일 주소입니다."),
    USERNAME_ALREADY_EXISTS("이미 사용 중인 사용자명입니다.")
    ;

    private final String description;


}
