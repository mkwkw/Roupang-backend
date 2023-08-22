package com.teamsupercat.roupangbackend.dto.product;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.common.DateUtils;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.List;

@Slf4j
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProductResponse {

    private Integer productIdx;
    private String productName;
    private Long price;
    private Integer stock;
    private String description;
    private String descriptionImg;
    private String categoryName;
    private String productImg;
    private String salesEndDate;
    private List<OptionTypeResponse> options;

    public ProductResponse toDto(Product product){

        String endDateStr;
        try{
           endDateStr = DateUtils.convertToString(product.getSalesEndDate());
        }
        catch (ParseException e){
            endDateStr = "판매 종료 날짜가 없습니다.";
        }

        ProductResponse productResponse = ProductResponse.builder()
                .productIdx(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .descriptionImg(product.getDescriptionImg())
                .productImg(product.getProductImg())
                .salesEndDate(endDateStr)
                .categoryName(product.getProductsCategoryIdx().getCategoryName())
                .build();

        return productResponse;
    }

    public ProductResponse toDto2(Product product, List<OptionTypeResponse> optionTypeResponseList) throws ParseException {

        return ProductResponse.builder()
                .productIdx(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .description(product.getDescription())
                .descriptionImg(product.getDescriptionImg())
                .productImg(product.getProductImg())
                .salesEndDate(DateUtils.convertToString(product.getSalesEndDate()))
                .categoryName(product.getProductsCategoryIdx().getCategoryName())
                .options(optionTypeResponseList)
                .build();
    }

}
