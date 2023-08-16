package com.teamsupercat.roupangbackend.dto.cart.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartPlusResponse {
    private Integer id;
    private Integer memberIdx;
    private Integer productIdx;
    private Integer amount;
    private String createdAt;

}
