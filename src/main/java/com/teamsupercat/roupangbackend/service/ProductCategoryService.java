package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.dto.product.ProductCategoryDto;
import com.teamsupercat.roupangbackend.repository.ProductsCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductsCategoryRepository productsCategoryRepository;

    public List<ProductCategoryDto> findProductCategoryDtoList(){
        return productsCategoryRepository.findAll().stream()
                .map(productsCategory -> new ProductCategoryDto().productCategoryToDto(productsCategory))
                .collect(Collectors.toList());
    }
}
