package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.cart.request.CartChangeRequest;
import com.teamsupercat.roupangbackend.dto.cart.response.CartAllResponse;
import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.repository.CartRepository;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // todo: 장바구니에 수량 설정 후 등록
    @Transactional
    public void cartProductPlus(Integer memberId, CartChangeRequest cartChangeRequest) {
        // 유저를 검색해 실제 있는 유저인지 확인
        Member bymember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_USER));
        // 추가하려는 상품이 실제 존재하는지 확인
        Product byproduct = productRepository.findById(cartChangeRequest.getProductIdx()).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_PRODUCT));

        if (byproduct.getStock() <= 0) {
            // 품절인경우 예외처리
            throw new CustomException(ErrorCode.NOTFOUND_PRODUCT_STOCK);
        } else if (byproduct.getStock() <= cartChangeRequest.getAmount()) {
            // 재고보다 많이 주문했을 시 예외처리
            throw new CustomMessageException(ErrorCode.OUT_OF_STOCK, "재고", String.valueOf(byproduct.getStock()));
        }
        // 장바구니에 제품 등록여부 확인
        Optional<Cart> byMemberIdAndProductId = cartRepository.findByMemberIdAndProductId(memberId, cartChangeRequest.getProductIdx());
        // Request -> Entity 형변환
        Cart saveToEntity = cartChangeRequest.saveToEntity(bymember, byproduct);

        if (byMemberIdAndProductId.isPresent()) {
            // 기존 상품의 inx 와 구매수량 수정
            saveToEntity.setId(byMemberIdAndProductId.get().getId());
            saveToEntity.setAmount(cartChangeRequest.getAmount());
        }
        // 저장 및 수정
        cartRepository.save(saveToEntity);

    }

    // todo: 장바구니 모두 비우기
    @Transactional
    public void cartProductDel(Integer member) {
        // 유저와 상품을 검색하여 존재여부 확인
        Member byMember = memberRepository.findById(member).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_USER));

        // 장바구니가 비어있지않은지 비교
        List<Cart> byMemberIdx = cartRepository.findByMemberIdxAndIsDeletedFalse(byMember);

        if (byMemberIdx.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_DEL_PRODUCT);
        }

        for (Cart cart : byMemberIdx) {
            // IsDeleted가 false인것만 true로 변경
            cart.setIsDeleted(true);
        }
        cartRepository.saveAll(byMemberIdx);


    }

    // todo: 해당 유저 장바구니 내역리스트 출력
    @Transactional
    public List<CartAllResponse> cartAllList(Integer memberId) {
        // 현재 유저를 검색하여 객체형태로 조회한다
        Member byMember = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.NOTFOUND_USER));
        // 나의 장바구니내역을 조회한다
        List<Cart> cartRepositoryByMemberIdx = cartRepository.findByMemberIdxAndIsDeletedFalse(byMember);


        return cartRepositoryByMemberIdx.stream().map(cart -> {
            CartAllResponse cartAllResponse = new CartAllResponse();
            return cartAllResponse.toEntity(byMember.getId(), cart);
        }).collect(Collectors.toList());

          /*
        // 반복문을 통해 엔티티객체를 response로 매핑
        List<CartAllResponse> commentGetResponse = new ArrayList<>();

        for (Cart cart : cartRepositoryByMemberIdx) {
            // 리스폰스 객체 생성
            CartAllResponse cartAllResponse = new CartAllResponse();
            //객체 매핑
            cartAllResponse = cartAllResponse.toEntity(byMember.getId(), cart);
            // 매핑된 객체 리스트에 추가
            commentGetResponse.add(cartAllResponse);
        }
        */
    }


}
