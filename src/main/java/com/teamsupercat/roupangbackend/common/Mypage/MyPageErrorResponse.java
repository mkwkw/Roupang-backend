package com.teamsupercat.roupangbackend.common.Mypage;

import com.teamsupercat.roupangbackend.common.Mypage.type.MyPageErrorCode;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageErrorResponse {
    private MyPageErrorCode myPageErrorCode;
    private String errorMessage;
}
