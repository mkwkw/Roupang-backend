package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.dto.cart.request.CartPlusRequest;
import com.teamsupercat.roupangbackend.dto.cart.response.CartAllResponse;
import com.teamsupercat.roupangbackend.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "장바구니 API")
@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @ApiOperation(value = "장바구니 상품추가")
    @PostMapping
    public String cartProductPlus(@RequestBody CartPlusRequest cartPlusRequest) {
        Integer user = 1;
        cartService.cartProductPlus(user, cartPlusRequest);

        return "장바구니 담기 완료";

    }


    // 미완성
    @ApiOperation(value = "장바구니 전체조회")
    @GetMapping
    public List<CartAllResponse> cartAllList() {
        Integer user = 1;

        return cartService.cartAllList(user);

    }

}
