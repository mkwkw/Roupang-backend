package com.teamsupercat.roupangbackend.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.common.DateUtils;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import lombok.*;

import java.text.ParseException;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {

    private String productName;
    private Long price;
    private Integer stock;
    private String description;
    private String descriptionImg;
    private String categoryName;
    private String productImg;
    private String salesEndDate;


    public ProductResponse toDto(Product product) throws ParseException {
        return ProductResponse.builder()
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .descriptionImg(product.getDescriptionImg())
                .productImg(product.getProductImg())
                .salesEndDate(DateUtils.convertToString(product.getSalesEndDate()))
                .categoryName(product.getProductsCategoryIdx().getCategoryName())
                .build();
    }

}
