package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.MyPageDto;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Seller;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final MemberRepository memberRepository;
    private final SellerRepository sellerRepository;

    @Transactional
    public ResponseDto<?> getMyPageInfo(String memberEmail) {

        Member member = memberRepository.findByEmail(memberEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.MY_PAGE_PERMISSION_DENIED));

        Seller seller = sellerRepository.findSellerByMemberIdx(member);

        MyPageDto.MyPageSellerResponse myPageResponse = MyPageDto.MyPageSellerResponse.toMemberSellerResponse(member,seller);

        return ResponseDto.success(myPageResponse);
    }


    @Transactional
    public MyPageDto.MyPageSellerResponse updateMyPage(
            MyPageDto.UpdateMyPageRequest updateMyPageRequest,
            String email){

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MY_PAGE_REQUIRED_INFO_MISSING));

        member.setNickname(updateMyPageRequest.getNickname());
        member.setPhoneNumber(updateMyPageRequest.getPhoneNumber());
        member.setAddress(updateMyPageRequest.getAddress());
        member.setMemberImg(updateMyPageRequest.getMemberImg());


        return MyPageDto.MyPageResponse.toResponse(member);

    }
}
