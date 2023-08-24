package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.order.request.PurchaseItemRequest;
import com.teamsupercat.roupangbackend.dto.payment.request.PaymentRequest;
import com.teamsupercat.roupangbackend.service.CartOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "결제상세 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class CartOrderController {

    private final CartOrderService cartOrderService;
    private final CartController cartController;


//    @ApiOperation(value = "주문 확인페이지 유저정보")
//    @GetMapping
//    public ResponseDto<?> purchaseMemberFromCart(@AuthenticationPrincipal CustomUserDetail userDetails, HttpServletRequest servletRequest) {
//        return ResponseDto.success(cartOrderService.purchaseMemberFromCart(cartController.authMemberCheck(userDetails, servletRequest)));
//    }

    @ApiOperation(value = "주문 확인페이지 유저정보, 장바구니 품목")
    @PostMapping
    public ResponseDto<?> purchaseItemsFromCart(@AuthenticationPrincipal CustomUserDetail userDetails, @RequestBody List<PurchaseItemRequest> purchaseItemRequest) {
        return ResponseDto.success(cartOrderService.purchaseItemsFromCart(cartController.authMemberCheck(userDetails), purchaseItemRequest));
    }

    @ApiOperation(value = "주문 결제")
    @PostMapping("/payment")
    public ResponseDto<?> paymentProcessing(@AuthenticationPrincipal CustomUserDetail userDetails, @RequestBody PaymentRequest paymentRequest) {
        cartOrderService.paymentProcessing(cartController.authMemberCheck(userDetails), paymentRequest);
        return ResponseDto.success("상품 구매가 완료되었습니다.");

    }
}
