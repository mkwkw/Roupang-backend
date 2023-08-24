package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.option.request.OptionRegisterRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.service.OptionService;
import com.teamsupercat.roupangbackend.service.ProductCategoryService;
import com.teamsupercat.roupangbackend.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Api(tags = "물품 API")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;
    private final ProductCategoryService productCategoryService;

    @ApiOperation(value= "판매 물품 상세 조회 ", notes = "물품 상세 조회")
    @GetMapping("/products/{product_id}")
    public ResponseDto<Object> getProductOne(@PathVariable("product_id") Integer productId) throws ParseException {

        ProductResponse productResponse = productService.getProductOne(productId);

        return new ResponseDto<>(true, "물품 조회에 성공하였습니다.", productResponse);

    }



    @ApiOperation("물품 조회 - 정렬 기준 설정 가능")
    @GetMapping("/products")
    public ResponseDto<?> findAllProducts(@RequestParam(value = "order", required = false) String order, Pageable pageable) throws ParseException {
        Page<ProductResponse> products = productService.findProductsPagination(order, pageable);
        return ResponseDto.success(products);
    }

    @ApiOperation("카테고리별 물품 조회")
    @GetMapping("/products/category/{category_idx}")
    public ResponseDto<?> findProductsByCategory(@PathVariable("category_idx") Integer categoryIdx, Pageable pageable, @RequestParam(value = "order", required = false, defaultValue = "") String order){
        Page<ProductResponse> products = productService.findProductsByCategoryIdxPagination(order, categoryIdx, pageable);
        return ResponseDto.success(products);
    }

    @ApiOperation("product_idx로 물품 상세 옵션 조회")
    @GetMapping("/products/option/{product_idx}")
    public ResponseDto<?> findOptionByProductIdx(@PathVariable("product_idx") Integer productIdx){
        Map<String, Object> options = optionService.findOptionByProductIdx(productIdx);
        return ResponseDto.success(options);
    }

    @ApiOperation("모든 카테고리 조회")
    @GetMapping("/products/category")
    public ResponseDto<?> findAllCategory(){
        return ResponseDto.success(productCategoryService.findProductCategoryDtoList());
    }

    @ApiOperation("모든 옵션 정보 조회")
    @GetMapping("/products/option")
    public ResponseDto<?> findAllOption(){
        return ResponseDto.success(optionService.findAllOption());
    }

    @ApiOperation("옵션으로 물품 조회")
    @GetMapping("/products/option/search")
    public ResponseDto<?> findProductsByOption(@RequestParam("option-name") List<String> options){
        return ResponseDto.success(optionService.findProductByOptionDetailName(options));
    }

    @ApiOperation("뭂품 옵션 등록")
    @PostMapping("/products/option/register")
    public ResponseDto<?> registerOptionOfProduct(@RequestBody OptionRegisterRequest optionRegisterRequest){
        return ResponseDto.success(optionService.registerOptionOfProduct(optionRegisterRequest));
    }

    @ApiOperation("물품 검색")
    @GetMapping("/products/search")
    public ResponseDto<?> searchProduct(@RequestParam("keyword") String keyword, @RequestParam(value = "order", required = false) String order, Pageable pageable){
        return ResponseDto.success(productService.searchProduct(keyword, order, pageable));
    }

    @ApiOperation("물품과 판매량 조회")
    @GetMapping("/products/sales")
    public ResponseDto<?> getProductSales(Pageable pageable){
        return ResponseDto.success(productService.getProductSales(pageable));
    }
}
