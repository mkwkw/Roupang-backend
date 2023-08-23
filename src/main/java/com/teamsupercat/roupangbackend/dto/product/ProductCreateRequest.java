package com.teamsupercat.roupangbackend.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.common.DateUtils;
import com.teamsupercat.roupangbackend.dto.option.request.OptionWithProductRegisterRequest;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import com.teamsupercat.roupangbackend.entity.Seller;
import lombok.*;

import java.text.ParseException;
import java.util.List;

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
    private Boolean existsOption;
    private List<OptionWithProductRegisterRequest> options;





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

    public void updateEntity(Product existingProduct, Seller sellerFound, Integer productId) throws ParseException {
        existingProduct.setId(productId);
        existingProduct.setSellerIdx(sellerFound);
        existingProduct.setProductName(getProductName());
        existingProduct.setDescription(getDescription());
        existingProduct.setPrice(getPrice());
        existingProduct.setStock(getStock());
        existingProduct.setSalesEndDate(DateUtils.convertToTimestamp(getSalesEndDate()));
        existingProduct.setProductsCategoryIdx(ProductsCategory.builder().id(getCategoryIdx()).build());
        existingProduct.setProductImg(getProductImg());
        existingProduct.setDescriptionImg(getDescriptionImg());
    }
}
