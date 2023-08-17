package com.teamsupercat.roupangbackend.dto.cart.request;

import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartChangeRequest {
    private Integer productIdx;
    private Integer amount;

    public Cart saveToEntity(Member bymember, Product byproduct) {
        return Cart.builder()
                .memberIdx(bymember)
                .productIdx(byproduct)
                .amount(amount)
                .createdAt(Instant.now())
                .isDeleted(false)
                .build();
    }
}
