package com.teamsupercat.roupangbackend.dto.viewhistory.response;

import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewHistoryResponse {


    private Integer productIdx;
    private String productName;
    private String productImg;
    private String description;
    private Long price;
    private Integer stock;

    public ViewHistoryResponse toEntity(Product product) {
        return ViewHistoryResponse.builder()
                .productIdx(product.getId())
                .productName(product.getProductName())
                .productImg(product.getProductImg())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }
}
