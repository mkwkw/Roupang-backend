package com.teamsupercat.roupangbackend.dto.cart.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartMinusResponse {
    private Integer id;
    private Integer memberIdx;
    private Integer productIdx;
    private Integer amount;
    private String createdAt;
}
