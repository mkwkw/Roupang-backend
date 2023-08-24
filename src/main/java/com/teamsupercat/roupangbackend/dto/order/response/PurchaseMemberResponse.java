package com.teamsupercat.roupangbackend.dto.order.response;

import com.teamsupercat.roupangbackend.entity.Member;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PurchaseMemberResponse {
    private Integer memberIdx;
    private String nickName;
    private String address;
    private String phoneNumber;
    private String email;
    private Long userPoint;
    private List<PurchaseItemResponse> purchaseItemResponseList;

    public PurchaseMemberResponse toMember(Member member) {
        return PurchaseMemberResponse.builder()
                .memberIdx(member.getId())
                .nickName(member.getNickname())
                .address(member.getAddress())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .userPoint(member.getUserPoint())
                .build();
    }
}
