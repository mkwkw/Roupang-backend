package com.teamsupercat.roupangbackend.dto;

import com.teamsupercat.roupangbackend.entity.Member;
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

        public static MyPageResponse toResponse(Member member){

            return MyPageResponse.builder()
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

        public static List<MyPageResponse> toResponse(List<Member> list){
            return list.stream().map(MyPageResponse::toResponse).collect(Collectors.toList());
        }

    }
}
