package com.teamsupercat.roupangbackend.dto.cart.request;

import com.teamsupercat.roupangbackend.dto.cart.response.CartPlusResponse;
import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartPlusRequest {
    private Product productIdx;


    public Cart toEntity(Integer user, CartPlusRequest cartPlusRequest) {
        return Cart.builder()
                .id(user)
                .productIdx(cartPlusRequest.productIdx)
                .build();
    }


}
