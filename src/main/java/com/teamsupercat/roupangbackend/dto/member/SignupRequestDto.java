package com.teamsupercat.roupangbackend.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupRequestDto {
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String memberImg;

}
