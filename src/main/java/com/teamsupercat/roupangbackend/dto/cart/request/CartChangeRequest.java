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
    private String optionDetail;

    public Cart saveToEntity(Member member, Product byproduct) {
        return Cart.builder()
                .memberIdx(member)
                .productIdx(byproduct)
                .amount(amount)
                .createdAt(Instant.now())
                .isDeleted(false)
                .optionDetail(getOptionDetail())
                .build();
    }
}
