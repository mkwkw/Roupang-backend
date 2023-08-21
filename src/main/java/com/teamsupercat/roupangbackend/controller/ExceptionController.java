package com.teamsupercat.roupangbackend.controller;


import com.teamsupercat.roupangbackend.common.ResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ExceptionController {
    @GetMapping("/exception")
    public ResponseDto<?> NotAuthenticationUser(){
        return ResponseDto.fail("해당 유저에게 권한이 없습니다.");
    }
}
