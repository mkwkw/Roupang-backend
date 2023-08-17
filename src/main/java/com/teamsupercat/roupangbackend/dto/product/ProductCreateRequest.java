package com.teamsupercat.roupangbackend.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.common.DateUtils;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import com.teamsupercat.roupangbackend.entity.Seller;
import io.swagger.models.auth.In;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductCreateRequest {

    private String productName;
    private String description;
    private Long price;
    private Integer stock;
    private String salesEndDate;
    private Integer categoryIdx;
    private String productImg;
    private String descriptionImg;


    public Product toEntity(ProductCreateRequest productCreateRequest, Seller seller) throws ParseException {
        return Product.builder()
                .sellerIdx(seller)
                .productName(productCreateRequest.getProductName())
                .description(productCreateRequest.getDescription())
                .price(productCreateRequest.getPrice())
                .stock(productCreateRequest.getStock())
                .salesEndDate(DateUtils.convertToTimestamp(productCreateRequest.getSalesEndDate()))
                .productsCategoryIdx(ProductsCategory.builder().id(productCreateRequest.getCategoryIdx()).build())
                .productImg(productCreateRequest.getProductImg())
                .descriptionImg(productCreateRequest.getDescriptionImg())
                .isDeleted(false)
                .build();

    }
}
