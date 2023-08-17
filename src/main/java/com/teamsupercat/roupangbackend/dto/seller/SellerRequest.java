package com.teamsupercat.roupangbackend.dto.seller;

import com.teamsupercat.roupangbackend.entity.Member;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SellerRequest {

    private Integer id;



}








//    public Member toEntity(SellerRequest sellerRequest) {
//        return Member.builder()
//                .id(sellerRequest.getId())
//                .build();
//    }