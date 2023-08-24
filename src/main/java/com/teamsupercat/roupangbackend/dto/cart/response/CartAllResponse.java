package com.teamsupercat.roupangbackend.dto.cart.response;

import com.teamsupercat.roupangbackend.entity.Cart;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartAllResponse {
    private Integer id;
    private Integer memberId;
    private Integer productIdx;
    private String categoryName;
    private Integer sellerIdx;
    private String productName;
    private String productImg;
    private String description;
    private Long price;
    private Integer amount;
    private Integer productStock;
    private String createdAt;
    private String optionDetail;

    public CartAllResponse toEntity(Integer memberId, Cart cart) {
        return CartAllResponse.builder()
                .id(cart.getId())
                .memberId(memberId)
                .productIdx(cart.getProductIdx().getId())
                .categoryName(cart.getProductIdx().getProductsCategoryIdx().getCategoryName())
                .sellerIdx(cart.getProductIdx().getSellerIdx().getId())
                .productName(cart.getProductIdx().getProductName())
                .productImg(cart.getProductIdx().getProductImg())
                .description(cart.getProductIdx().getDescription())
                .price(cart.getProductIdx().getPrice())
                .amount(cart.getAmount())
                .productStock(cart.getProductIdx().getStock())
                .createdAt(cart.getCreatedAt().toString())
                .optionDetail(cart.getOptionDetail())
                .build();
    }
}
