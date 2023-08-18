package com.teamsupercat.roupangbackend.common.Mypage;


import com.teamsupercat.roupangbackend.common.Mypage.type.MyPageErrorCode;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class MyPageException extends RuntimeException {

    private MyPageErrorCode myPageErrorCode;
    private String errorMessage;

    public MyPageException(MyPageErrorCode myPageErrorCode){
        this.myPageErrorCode = myPageErrorCode;
        this.errorMessage = myPageErrorCode.getDescription();
    }

}
