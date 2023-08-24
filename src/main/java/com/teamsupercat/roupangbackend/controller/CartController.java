package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.cart.request.CartChangeRequest;
import com.teamsupercat.roupangbackend.dto.cart.request.RemoveCartRequest;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Stream;

@Api(tags = "장바구니 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final MemberRepository memberRepository;

    @ApiOperation(value = "장바구니 상품추가")
    @PostMapping
    public ResponseDto<?> cartProductPlus(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody CartChangeRequest cartChangeRequest) {
        hasNullFieldsCartPlusRequest(cartChangeRequest);
        cartService.cartProductPlus(authMemberCheck(userDetail), cartChangeRequest);
        return ResponseDto.success("상품 및 수량이 장바구니에 등록, 수정되었습니다.");
    }

    @ApiOperation(value = "장바구니 전체조회")
    @GetMapping
    public ResponseDto<?> cartAllList(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return ResponseDto.success(cartService.cartAllList(authMemberCheck(userDetail)));
    }

    @ApiOperation(value = "장바구니 낱개 제거")
    @PatchMapping
    public ResponseDto<?> removeCartItem(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody RemoveCartRequest RemoveCartRequest) {
        cartService.removeCartItem(authMemberCheck(userDetail), RemoveCartRequest);
        return ResponseDto.success("상품이 삭제되었습니다.");
    }

    @ApiOperation(value = "장바구니 상품 비우기")
    @DeleteMapping
    public ResponseDto<?> cartProductDel(@AuthenticationPrincipal CustomUserDetail userDetail) {
        cartService.cartProductDel(authMemberCheck(userDetail));
        return ResponseDto.success("나의 장바구니 물품을 모두 삭제했습니다.");
    }

    // 헤더가 존재하는지 확인 후 존재한다면 유저를 검색하여 인증하고 인증된 유저객체를 반환
    public Member authMemberCheck(CustomUserDetail userDetail) {
        return memberRepository.findById(userDetail.getMemberIdx()).orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND_TOKEN));
    }

    // request 객체에서 비어있는 값이 있는지 확인하는 코드
    private void hasNullFieldsCartPlusRequest(CartChangeRequest request) {
        if (Stream.of(request.getProductIdx(), request.getAmount()).anyMatch(Objects::isNull)) {
            throw new CustomException(ErrorCode.NULL_FIELDS_REQUEST);
        }
    }
}
