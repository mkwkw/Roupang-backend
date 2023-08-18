package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.cart.request.CartChangeRequest;
import com.teamsupercat.roupangbackend.dto.cart.request.RemoveCartRequest;
import com.teamsupercat.roupangbackend.dto.cart.response.CartAllResponse;
import com.teamsupercat.roupangbackend.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Api(tags = "장바구니 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @ApiOperation(value = "장바구니 상품추가")
    @PostMapping
    public ResponseDto<?> cartProductPlus(@AuthenticationPrincipal CustomUserDetail userDetails, @RequestBody CartChangeRequest cartChangeRequest) {
        Integer memberId = userDetails.getMemberIdx();
        if (hasNullFieldsCartPlusRequest(cartChangeRequest)) {
            throw new CustomException(ErrorCode.NULL_FIELDS_REQUEST);
        }
        cartService.cartProductPlus(memberId, cartChangeRequest);

        return ResponseDto.success("상품 및 수량이 장바구니에 등록, 수정되었습니다.");
    }

    // request 객체에서 비어있는 값이 있는지 확인하는 코드
    private boolean hasNullFieldsCartPlusRequest(CartChangeRequest request) {
        return Stream.of(request.getProductIdx(), request.getAmount()).anyMatch(Objects::isNull);
    }

    //    @ApiOperation(value = "장바구니 전체조회")
//    @GetMapping
//    public List<CartAllResponse> cartAllList(@AuthenticationPrincipal CustomUserDetail userDetails) {
//        Integer memberId = userDetails.getMemberIdx();
//        userDetails.getMemberIdx();
//        return cartService.cartAllList(memberId);
//    }
    @ApiOperation(value = "장바구니 전체조회")
    @GetMapping
    public ResponseDto<List<CartAllResponse>> cartAllList(@AuthenticationPrincipal CustomUserDetail userDetails) {
        Integer memberId = userDetails.getMemberIdx();
        userDetails.getMemberIdx();

        return ResponseDto.success(cartService.cartAllList(memberId));
    }

    @ApiOperation(value = "장바구니 낱개 제거")
    @PatchMapping
    public ResponseDto<?> removeCartItem(@AuthenticationPrincipal CustomUserDetail userDetail, @RequestBody RemoveCartRequest RemoveCartRequest) {

        cartService.removeCartItem(userDetail, RemoveCartRequest);
        return ResponseDto.success(null);
    }

    @ApiOperation(value = "장바구니 상품 비우기")
    @DeleteMapping("/cart_del")
    public ResponseDto<?> cartProductDel(@AuthenticationPrincipal CustomUserDetail userDetails) {
        Integer memberId = userDetails.getMemberIdx();
        cartService.cartProductDel(memberId);

        return ResponseDto.success("나의 장바구니 물품을 모두 삭제했습니다.");

    }
}
