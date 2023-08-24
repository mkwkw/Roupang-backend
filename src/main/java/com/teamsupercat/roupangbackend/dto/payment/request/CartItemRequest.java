package com.teamsupercat.roupangbackend.dto.payment.request;

import com.teamsupercat.roupangbackend.entity.GroupedOrder;
import com.teamsupercat.roupangbackend.entity.SingleOrder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {
    private Integer singleOrderNum;
    private Integer amount;// 상품갯수
    public GroupedOrder toEntity(String groupNum, SingleOrder order) {
        return GroupedOrder.builder()
                .groupedId(groupNum)
                .singleOrders(order)
                .build();
    }

}