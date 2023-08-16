package com.teamsupercat.roupangbackend.dto.cart.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartMinusRequest {
    private Integer id;
    private Integer memberIdx;
    private Integer productIdx;
    private Integer amount;
    private String createdAt;
}
