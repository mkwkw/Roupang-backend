package com.teamsupercat.roupangbackend.dto.cart.response;

import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAllResponse {
    private Integer id;
    private Product productIdx;
    private Integer amount;
    private String createdAt;

    public Cart toEntity(Integer user , CartAllResponse cartAllResponse) {
        return Cart.builder()
                .id(cartAllResponse.id)
                .productIdx(cartAllResponse.productIdx)
                .build();
    }
}
