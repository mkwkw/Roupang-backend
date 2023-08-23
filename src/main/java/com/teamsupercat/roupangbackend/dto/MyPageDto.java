package com.teamsupercat.roupangbackend.dto;

import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Seller;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class MyPageDto {

    @ToString
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateMyPageRequest {
        private String nickname;
        private String phoneNumber;
        private String address;
        private String memberImg;
    }


    @ToString
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageSellerResponse {

        private Integer memberIdx;
        private String nickname;
        private String phoneNumber;
        private String email;

        private String address;
        private String memberImg;
        private boolean seller;

        private Long userPoint;

        private Instant createdAt;
        private Instant updatedAt;

        public static MyPageSellerResponse toMemberSellerResponse(Member member, Seller seller){
            return MyPageSellerResponse.builder()
                    .memberIdx(member.getId())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .seller(seller !=null)
                    .memberImg(member.getMemberImg())
                    .userPoint(member.getUserPoint())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .build();
        }

    }

    @ToString
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {

        private Integer memberIdx;
        private String nickname;
        private String phoneNumber;
        private String email;

        private String address;
        private String memberImg;

        private Long userPoint;

        private Instant createdAt;
        private Instant updatedAt;

        public static MyPageSellerResponse toResponse(Member member){
            return MyPageSellerResponse.builder()
                    .memberIdx(member.getId())
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .email(member.getEmail())
                    .address(member.getAddress())
                    .memberImg(member.getMemberImg())
                    .userPoint(member.getUserPoint())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .build();
        }

    }
}
