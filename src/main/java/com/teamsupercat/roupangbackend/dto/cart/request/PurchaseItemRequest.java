package com.teamsupercat.roupangbackend.dto.cart.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemRequest {
    private Integer productIdx;
    private Integer amount;

}
