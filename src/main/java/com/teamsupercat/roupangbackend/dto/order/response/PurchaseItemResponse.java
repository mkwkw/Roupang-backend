package com.teamsupercat.roupangbackend.dto.order.response;

import com.teamsupercat.roupangbackend.entity.SingleOrder;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseItemResponse {
    private Integer singleOrderNum;
    private String productName;
    private String productImg;
    private String description;
    private Integer amount;// 상품갯수
    private Long allPrice;// 상품별 총 가격 (상품갯수*상품가격)
    private String option;

    public PurchaseItemResponse toSingleOrderList(SingleOrder singleOrder) {
        return PurchaseItemResponse.builder()
                .singleOrderNum(singleOrder.getId())
                .productName(singleOrder.getProductIdx().getProductName())
                .productImg(singleOrder.getProductIdx().getProductImg())
                .description(singleOrder.getProductIdx().getDescription())
                .amount(singleOrder.getAmount())
                .allPrice(singleOrder.getAmount() * singleOrder.getProductIdx().getPrice())
                .option(singleOrder.getOptionDetail())
                .build();
    }
}
