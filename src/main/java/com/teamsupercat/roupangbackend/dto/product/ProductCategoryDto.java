package com.teamsupercat.roupangbackend.dto.product;

import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryDto {

    private Integer categoryIdx;
    private String categoryName;

    public ProductCategoryDto productCategoryToDto(ProductsCategory productsCategory){
        return new ProductCategoryDto(productsCategory.getId(), productsCategory.getCategoryName());
    }

}
