package com.teamsupercat.roupangbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //회원가입
    SIGNUP_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    SIGNUP_DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    SIGNUP_DUPLICATE_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "중복된 전화번호입니다."),
    SIGNUP_CHECK_EMAIL(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    SIGNUP_CHECK_NICKNAME(HttpStatus.BAD_REQUEST, "이미 존재하는 닉네임입니다."),
    SIGNUP_CHECK_FAIL(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),

    //회원탈퇴
    SIGN_OUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "토큰에서 이메일을 찾을수 없습니다."),

    //로그인
    LOGIN_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "등록되어 있지 않은 이메일 입니다."),
    LOGIN_NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "등록된 회원의 정보와 일치하지 않습니다."),
    LOGIN_NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "맴버에 해당하는 토큰을 찾을수 없습니다."),
    LOGIN_BEFORE_ALLOWED_LOGIN_TIME(HttpStatus.UNAUTHORIZED, "로그인 시도 횟수초과로 일시적으로 잠금됩니다."),

    //로그아웃
    LOGOUT_NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST, "등록되어 있지 않은 이메일 입니다."),
    LOGOUT_NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "맴버에 해당하는 토큰을 찾을수 없습니다."),

    // 마이페이지
    MY_PAGE_RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"요청한 정보를 찾을 수 없습니다."),
    MY_PAGE_PERMISSION_DENIED(HttpStatus.FORBIDDEN,"권한이 거부되었습니다."),
    MY_PAGE_EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT,"이미 등록된 이메일 주소입니다."),
    MY_PAGE_NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED,"권한이 없습니다. 로그인 후 이용해주세요."),
    MY_PAGE_REQUIRED_INFO_MISSING(HttpStatus.BAD_REQUEST, "필수정보가 누락되었습니다."),

    // REQUEST
    NULL_FIELDS_REQUEST(HttpStatus.NOT_FOUND, "요청 중 일부 필드가 누락되었습니다."),

    // 게시글
    SHOP_POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "찾을수 없는 게시글입니다."),

    // 유저
    SHOP_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    CART_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    SHOP_PRODUCT_ONLY_SELLERS(HttpStatus.BAD_REQUEST, "판매자로 등록된 유저만 사용할 수 있습니다."),
    SHOP_MISMATCH_SELLER(HttpStatus.UNAUTHORIZED, "해당 물품의 판매자와 현재 판매자가 일치하지 않습니다."),

    // 상품
    CART_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 물품이 존재하지 않습니다."),
    SHOP_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 물품이 존재하지 않습니다."),
    CART_NOTFOUND_PRODUCT_STOCK(HttpStatus.NOT_FOUND, "현재 상품은 품절입니다."),
    CART_OUT_OF_STOCK(HttpStatus.NOT_FOUND, "개의 상품 수량보다 많이 선택하셨습니다."),
    SHOP_OPTION_TYPE_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "옵션 타입을 찾을 수 없습니다."),
    SHOP_BAD_SORT_REQUEST(HttpStatus.BAD_REQUEST, "올바른 정렬 기준이 아닙니다."),

    //판매자
    SELLER_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),
    SELLER_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    SELLER_PRODUCT_EMPTY_LIST(HttpStatus.NO_CONTENT, "판매하는 제품이 없습니다."),
    SELLER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 판매자로 등록되어 있습니다."),

    // 카테고리
    SHOP_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),
    SHOP_CATEGORY_PRODUCT_EMPTY_LIST(HttpStatus.NOT_FOUND, "해당 카테고리에 물품이 존재하지 않습니다."),

    // 장바구니
    CART_EMPTY_PRODUCT(HttpStatus.NOT_FOUND, "장바구니에 담긴 항목이 없습니다."),
    CART_ITEM_NOT_PRODUCT(HttpStatus.NOT_FOUND, "장바구니에 담겨있지 않은 항목입니다."),

    // 결제수단
    CART_PAYMENT_METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "사용이 불가하거나 존재하지 않은 결제수단 입니다."),

    // 결제
    CART_ORDER_PRODUCT_OUT_OF_STOCK(HttpStatus.NOT_FOUND, "개 재고가 부족하여 구매할 수 없습니다."),
    CART_ORDER_INSUFFICIENT_USER_POINT(HttpStatus.NOT_FOUND, "원 입니다 포인트가 부족합니다."),
    CART_ORDER_ALREADY_PURCHASED(HttpStatus.BAD_REQUEST, "상품을 이미 구매하였습니다.."),

    // 개별주문 테이블
    CART_SINGLE_ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "개별주문 항목을 가져오지 못했습니다."),
    CART_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "선택한 옵션이 존재하지 않습니다."),


    // 리프래시 토큰
    REFRESH_TOKEN_NOT_FOUND_MEMBER_IDX(HttpStatus.BAD_REQUEST, "해당 유저의 식별값을 찾을 수 없습니다."),
    REFRESH_TOKEN_NOT_VALID_TOKEN(HttpStatus.BAD_REQUEST, "해당 토큰은 유효하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "해당 이메일은 존재하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "맴버에 해당하는 토큰을 찾을수 없습니다."),
    REFRESH_TOKEN_REFRESH_FAILED(HttpStatus.BAD_REQUEST, "토큰 재발급의 실패 하였습니다."),


    // 이메일 인증
    EMAIL_VERIFICATION_NOT_FOUND_USER(HttpStatus.NOT_FOUND , "해당 유저를 찾을 수 없습니다."),
    EMAIL_VERIFICATION_CODE_FAILED(HttpStatus.BAD_REQUEST,"인증 번호가 틀립니다."),
    EMAIL_VERIFICATION_EXPIRED_DATE(HttpStatus.BAD_REQUEST,"인증 시간이 만료되었습니다."),
    EMAIL_VERIFICATION_SEND_FAILED(HttpStatus.BAD_REQUEST,"인증 메일 전송에 실패하였습니다.")

    ;

    private final HttpStatus httpStatus;
    private final String errorMsg;

}