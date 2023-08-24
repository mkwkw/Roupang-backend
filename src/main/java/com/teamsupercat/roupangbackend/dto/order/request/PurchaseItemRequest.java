package com.teamsupercat.roupangbackend.dto.order.request;

import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.SingleOrder;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemRequest {
    private Integer productIdx;
    private Integer amount;
    private String optionDetail;


    public SingleOrder toEntity(Member member, PurchaseItemRequest purchaseItemRequest, Product product, Instant now) {
        return SingleOrder.builder()
                .memberIdx(member)
                .productIdx(product)
                .amount(purchaseItemRequest.getAmount())
                .orderDate(now)
                .optionDetail(purchaseItemRequest.getOptionDetail())
                .build();
    }
}
