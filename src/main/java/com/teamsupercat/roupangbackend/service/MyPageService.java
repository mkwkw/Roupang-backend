package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.Mypage.MyPageException;
import com.teamsupercat.roupangbackend.common.Mypage.type.MyPageErrorCode;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member getMyPage(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MyPageException(MyPageErrorCode.RESOURCE_NOT_FOUND));
    }

}
