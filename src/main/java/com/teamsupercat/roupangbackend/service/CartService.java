package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.dynamicMessageException.CustomMessageException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.cart.request.CartChangeRequest;
import com.teamsupercat.roupangbackend.dto.cart.request.RemoveCartRequest;
import com.teamsupercat.roupangbackend.dto.cart.response.CartAllResponse;
import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.repository.CartRepository;
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
    private final ProductRepository productRepository;

    // todo: 장바구니에 수량 설정 후 등록
    @Transactional
    public void cartProductPlus(Member member, CartChangeRequest cartChangeRequest) {

        // 추가하려는 상품 검색
        Product product = productRepository.findById(cartChangeRequest.getProductIdx()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND));

        // 상품이 품절이거나 재고보다 많이 담으려하면 예외처리
        if (product.getStock() <= 0) {
            throw new CustomException(ErrorCode.CART_NOTFOUND_PRODUCT_STOCK);
        } else if (product.getStock() < cartChangeRequest.getAmount()) {
            throw new CustomMessageException(ErrorCode.CART_OUT_OF_STOCK, "재고", String.valueOf(product.getStock()));
        }

        // 장바구니에 제품 등록여부 확인(아래에서 사용하므로 예외처리 X)
        Optional<Cart> optionalCart = cartRepository.findByMemberIdAndProductId(member.getId(), cartChangeRequest.getProductIdx());

        // Request -> Entity 형변환
        Cart saveToEntity = cartChangeRequest.saveToEntity(member, product);

        // 기존 상품이 없다면 신규생성, 기존 상품이 있다면 구매수량 수정
        if (optionalCart.isPresent()) {
            saveToEntity.setId(optionalCart.get().getId());
            saveToEntity.setAmount(cartChangeRequest.getAmount());
        }

        // 저장 및 수정
        cartRepository.save(saveToEntity);
    }

    // todo: 해당 유저 장바구니 내역리스트 출력
    @Transactional
    public List<CartAllResponse> cartAllList(Member member) {

        // 나의 장바구니내역을 조회한다
        List<Cart> cartList = cartRepository.findByMemberIdxAndIsDeletedFalse(member);

        // Cart타입 객체를 cartAllResponse형태로 형변환
        return cartList.stream().map(cart -> {
            CartAllResponse cartAllResponse = new CartAllResponse();
            return cartAllResponse.toEntity(member.getId(), cart);
        }).collect(Collectors.toList());
    }

    // todo 상품 단일 제거
    @Transactional
    public void removeCartItem(Member member, RemoveCartRequest request) {
        // 삭제할 상품 검색
        Product product = productRepository.findById(request.getProductDel()).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        // 삭제할 상품이 장바구니에 담겨있는지 확인
        Optional<Cart> optionalCartProduct = cartRepository.findByMemberIdxAndProductIdxAndIsDeletedFalse(member, product);

        // 장바구니에 상품이 담겨있지 않으면 예외처리
        if (optionalCartProduct.isEmpty()) {
            throw new CustomException(ErrorCode.CART_ITEM_NOT_PRODUCT);
        }

        // 해당 상품번호를 가진 아이템을 장바구니에서 삭제
        cartRepository.updateIsDeletedByMemberIdxAndProductIdx(member, product);
    }

    // todo: 장바구니 모두 비우기
    @Transactional
    public void cartProductDel(Member member) {

        // 유저 장바구니에 담겨있는 상품 리스트 출력
        List<Cart> myCartList = cartRepository.findByMemberIdxAndIsDeletedFalse(member);

        // 장바구니에 담겨있는 상품이 없을 시 예외처리
        if (myCartList.isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY_PRODUCT);
        }

        // 장바구니 상품을 하나씩 논리삭제
        myCartList.forEach(cart -> cart.setIsDeleted(true));

        // 삭제가 완료된 상품을 장바구니에 저장
        cartRepository.saveAll(myCartList);
    }
}
