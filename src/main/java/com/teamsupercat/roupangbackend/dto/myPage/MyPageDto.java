package com.teamsupercat.roupangbackend.dto.myPage;

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
    public static class GetMyPageRequest {
        private Integer memberIdx;
    }


    @ToString
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageResponse {

        private String nickname;
        private String phoneNumber;
        private String email;
        private String userPassword;

        private String address;
        private String memberImg;

        private Long userPoint;

        private Instant createdAt;
        private Instant updatedAt;
        private Boolean isDeleted;

        public static MyPageResponse toResponse(Member member){

            return MyPageResponse.builder()
                    .nickname(member.getNickname())
                    .phoneNumber(member.getPhoneNumber())
                    .email(member.getEmail())
                    .userPassword(member.getUserPassword())
                    .address(member.getAddress())
                    .memberImg(member.getMemberImg())
                    .userPoint(member.getUserPoint())
                    .createdAt(member.getCreatedAt())
                    .updatedAt(member.getUpdatedAt())
                    .isDeleted(member.getIsDeleted())
                    .build();
        }

        public static List<MyPageResponse> toResponse(List<Member> list){
            return list.stream().map(MyPageResponse::toResponse).collect(Collectors.toList());
        }

    }
}
