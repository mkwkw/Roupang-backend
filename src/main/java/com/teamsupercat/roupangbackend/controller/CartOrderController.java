package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.cart.request.PurchaseItemRequest;
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

@Api(tags = "장바구니결제 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class CartOrderController {

    private final CartOrderService cartOrderService;

    @ApiOperation(value = "장바구니 상품 결제기능")
    @PostMapping
    public ResponseDto<?> purchaseItemsFromCart(@AuthenticationPrincipal CustomUserDetail userDetails, @RequestBody List<PurchaseItemRequest> purchaseItemRequest) {
        Integer memberId = userDetails.getMemberIdx();
        cartOrderService.purchaseItemsFromCart(memberId, purchaseItemRequest);

        return ResponseDto.success(null);
    }


}
