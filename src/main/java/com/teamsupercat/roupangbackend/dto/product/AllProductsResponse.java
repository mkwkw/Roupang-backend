package com.teamsupercat.roupangbackend.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;
import org.springframework.data.domain.Page;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AllProductsResponse {

    private String productName;
    private String productImg;
    private String description;
    private Long price;


    public static AllProductsResponse fromProduct(Product product) {
        return AllProductsResponse.builder()
                .productName(product.getProductName())
                .productImg(product.getProductImg())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
