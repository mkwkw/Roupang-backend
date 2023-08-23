package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.MyPageDto;
import com.teamsupercat.roupangbackend.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(tags = "마이페이지 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    @ApiOperation(value = "마이페이지 조회")
    @GetMapping
    public ResponseDto<?> getMyPage(
            @AuthenticationPrincipal UserDetails userDetails,
            HttpServletRequest servletRequest) {

        // servletRequest.getHeader("authorization") : 추후 수정예정
        if (servletRequest.getHeader("authorization") == null || userDetails == null)
            throw new CustomException(ErrorCode.MY_PAGE_NOT_AUTHORIZED);

        log.info("userDetails1 : {}", userDetails);
        String memberEmail = userDetails.getUsername();

        return myPageService.getMyPageInfo(memberEmail);
    }

    @ApiOperation(value = "마이페이지 수정")
    @PatchMapping
    public ResponseDto<?> updateMyPage(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody MyPageDto.UpdateMyPageRequest updateMyPageRequest){

        String memberEmail = userDetails.getUsername();
        return ResponseDto.success(myPageService.updateMyPage(updateMyPageRequest, memberEmail));
    }

}


