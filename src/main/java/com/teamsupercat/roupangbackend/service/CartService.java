package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.dto.cart.request.CartPlusRequest;
import com.teamsupercat.roupangbackend.dto.cart.response.CartAllResponse;
import com.teamsupercat.roupangbackend.entity.Cart;
import com.teamsupercat.roupangbackend.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public void cartProductPlus(Integer user, CartPlusRequest cartPlusRequest) {
        // 유저를 검색해 실제 있는 유저인지 확인
        // 추가하려는 상품이 실제 존재하는지 확인
        // 추가하려는 상품의 재고가 남아있는지 확인
        // 장바구니에 제품이 없으면 장바구니에 상품추가
        // 장바구니에 물건이 있으면 상품갯수 +1
        //

        Cart cartEntity = cartPlusRequest.toEntity(user, cartPlusRequest);

        cartRepository.save(cartEntity);
    }


    public List<CartAllResponse> cartAllList(Integer user) {
        // 현재 유저를 검색하여 객체형태로 조회한다

    //        cartRepository.
    //        Optional<Cart> cartRepositoryById = cartRepository.findById(user);
    //
    //        for(){
    //
    //        }

    //        List<Optional<Cart>> collect = cartRepositoryById.stream().map(Cart -> cartRepositoryById).collect(Collectors.toList());
        log.info("정지");
        return null;
    }


}
