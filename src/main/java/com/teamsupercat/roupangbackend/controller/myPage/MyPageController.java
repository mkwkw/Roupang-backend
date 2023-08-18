package com.teamsupercat.roupangbackend.controller.myPage;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "마이페이지 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mypage")
public class MyPageController {

    private final MyPageService myPageService;

//    @ApiOperation(value = "마이페이지 조회")
//    @GetMapping(value = "/{email}")
//    public Member GetMyPage(@PathVariable("email") String email) {
//        return myPageService.getMyPage(email);
//    }

    @ApiOperation(value = "마이페이지 조회")
    @GetMapping(value = "/{email}")
    public ResponseDto<?> GetMyPage(@PathVariable("email") String email) {
        
        return ResponseDto.success(myPageService.getMyPage(email));
    }

}


