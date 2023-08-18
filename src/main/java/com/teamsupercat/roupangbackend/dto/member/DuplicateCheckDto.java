package com.teamsupercat.roupangbackend.dto.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DuplicateCheckDto {
    private String email;

    private String nickname;
}
