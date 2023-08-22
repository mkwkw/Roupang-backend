package com.teamsupercat.roupangbackend.dto.order.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemRequest {
    private Integer productIdx;
    private Integer amount;
    //TODO: 프론트로부터 상품의 옵션 값도 포함해서 받아야함
}
