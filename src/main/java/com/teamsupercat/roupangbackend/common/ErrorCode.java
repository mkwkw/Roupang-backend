package com.teamsupercat.roupangbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //회원가입
    SIGNUP_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이메일입니다."),
    SIGNUP_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,"중복된 닉네임입니다."),
    SIGNUP_DUPLICATE_PHONENUMBER(HttpStatus.BAD_REQUEST,"중복된 전화번호입니다."),
    SIGNUP_CHECK_EMAIL(HttpStatus.BAD_REQUEST,"이미 존재하는 이메일입니다."),
    SIGNUP_CHECK_NICKNAME(HttpStatus.BAD_REQUEST,"이미 존재하는 닉네임입니다."),
    SIGNUP_CHECK_FAIL(HttpStatus.BAD_REQUEST,"입력값이 올바르지 않습니다."),

    //회원탈퇴
    SIGNOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"토큰에서 이메일을 찾을수 없습니다."),

    //로그인
    LOGIN_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"등록되어 있지 않은 이메일 입니다."),
    LOGIN_NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST,"등록된 회원의 정보와 일치하지 않습니다."),
    LOGIN_NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST,"맴버에 해당하는 토큰을 찾을수 없습니다."),

    //로그아웃
    LOGOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"등록되어 있지 않은 이메일 입니다."),
    LOGOUT_NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST,"맴버에 해당하는 토큰을 찾을수 없습니다."),

    // REQUEST
    NULL_FIELDS_REQUEST(HttpStatus.NOT_FOUND, "요청 중 일부 필드가 누락되었습니다."),

    // 게시글
    POST_NOTFOUND(HttpStatus.BAD_REQUEST, "찾을수 없는 게시글입니다."),

    // 유저
    USER_NOTFOUND(HttpStatus.NOT_FOUND,"해당 유저가 존재하지 않습니다."),

    NOTFOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

    SELLER_ONLY(HttpStatus.BAD_REQUEST, "판매자로 등록된 유저만 사용할 수 있습니다."),


    // 상품
    PRODUCT_NOTFOUND(HttpStatus.NOT_FOUND,"해당 물품이 존재하지 않습니다." ),

    NOTFOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),

    NOTFOUND_PRODUCT_STOCK(HttpStatus.NOT_FOUND, "현재 상품은 품절입니다."),

    OUT_OF_STOCK(HttpStatus.NOT_FOUND, "개의 상품 재고수량보다 많이 선택하셨습니다."),

    // 카테고리
    CATEGORY_NOTFOUND(HttpStatus.NOT_FOUND,"해당 카테고리가 존재하지 않습니다." ),


    // 장바구니
    NOT_DEL_PRODUCT(HttpStatus.NOT_FOUND, "장바구니에 담긴 항목이 없습니다."),

    ;
    private final HttpStatus httpStatus;
    private final String errorMsg;

}