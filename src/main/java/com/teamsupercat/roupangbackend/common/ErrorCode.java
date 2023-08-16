package com.teamsupercat.roupangbackend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //
    NOTFOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),

    // 게시글
    POST_NOTFOUND(HttpStatus.BAD_REQUEST, "찾을수 없는 게시글입니다."),

    // 상품
    NOTFOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),

    NOTFOUND_PRODUCT_STOCK(HttpStatus.NOT_FOUND, "해당 상품의 재고가 남아있지 않습니다."),

    OUT_OF_STOCK(HttpStatus.NOT_FOUND, "개의 상품 재고수량보다 많이 선택하셨습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String errorMsg;

}