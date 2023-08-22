package com.teamsupercat.roupangbackend.dto.order.response;

import com.teamsupercat.roupangbackend.dto.order.request.PurchaseItemRequest;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.SingleOrder;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseItemResponse {
    // 구매자가 판매자에게 하고싶은 메시지
    private Integer memberIdx;
    private String nickName;
    private String address;
    private String phoneNumber;
    private String email;
    private Long userPoint;
    private Integer productIdx;
    private String productName;
    private String productImg;
    private String description;
    private Integer amount;// 상품갯수
    private Long allPrice;// 상품별 총 가격 (상품갯수*상품가격)
    private Integer deliveryMoney;// 배송비


    public PurchaseItemResponse toMember(Member member) {
        return PurchaseItemResponse.builder()
                .memberIdx(member.getId())
                .nickName(member.getNickname())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .userPoint(member.getUserPoint())
                .build();
    }

    public PurchaseItemResponse toSingleOrderList(SingleOrder singleOrder) {
        return PurchaseItemResponse.builder()
                .productIdx(singleOrder.getProductIdx().getId())
                .productName(singleOrder.getProductIdx().getProductName())
                .productImg(singleOrder.getProductIdx().getProductImg())
                .description(singleOrder.getProductIdx().getDescription())
                .amount(singleOrder.getAmount())
                .allPrice(singleOrder.getAmount() * singleOrder.getProductIdx().getPrice())
                .build();
    }
}
