package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.option.request.OptionRegisterRequest;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @ApiOperation(value= "판매자 등록", notes = "판매자로 등록하기")
    @PostMapping("/seller/signup")
    public ResponseDto<Object> resisterAsSeller(@AuthenticationPrincipal CustomUserDetail userDetails){

        Integer memberId = userDetails.getMemberIdx();

        productService.saveAsSeller(memberId);

        return new ResponseDto<>(true, "판매자로 등록되었습니다.", null);
    }


    @ApiOperation(value= "판매 물품 등록 ", notes = "판매할 물품 등록하기")
    @PostMapping("/seller/products/register")
    public ResponseDto<Object> createProduct(@RequestBody ProductCreateRequest productCreateRequest, @AuthenticationPrincipal CustomUserDetail userDetails) throws IOException, ParseException {

        Integer memberId = userDetails.getMemberIdx();

        productService.createProduct(productCreateRequest, memberId);

        return new ResponseDto<>(true, "물품 등록에 성공하였습니다.",null);

    }

    @ApiOperation(value= "판매 물품 상세 조회 ", notes = "물품 상세 조회")
    @GetMapping("/products/{product_id}")
    public ResponseDto<Object> getProductOne(@PathVariable("product_id") Integer productId) throws ParseException {

        ProductResponse productResponse = productService.getProductOne(productId);

        return new ResponseDto<>(true, "물품 조회에 성공하였습니다.", productResponse);

    }


    @ApiOperation(value= "판매자의 판매 물품 리스트 조회 ", notes = "판매자의 판매 물품 리스트 조회")
    @GetMapping("/seller/products")
    public ResponseDto<Object> getProductsList(@RequestParam(value = "order", required = false) String order, Pageable pageable, @AuthenticationPrincipal CustomUserDetail userDetails){

        Integer memberId = userDetails.getMemberIdx();

        List<AllProductsResponse> productsList = productService.getProductsList(order, pageable, memberId);

        return new ResponseDto<>(true, "판매자의 판매 물품 내역 조회에 성공하였습니다.", productsList);
    }

    @ApiOperation(value= "판매자의 판매 물품 수정 ", notes = "판매자의 판매 물품 수정")
    @PatchMapping("/seller/products/{product_id}")
    public ResponseDto<Object> updateProduct(@PathVariable("product_id") Integer productId,
                                             @RequestBody ProductCreateRequest productCreateRequest,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) throws ParseException {

        Integer memberId = userDetails.getMemberIdx();
        productService.updateProduct(productId, memberId, productCreateRequest);

        return new ResponseDto<>(true, "판매자의 판매 물품이 정상적으로 업데이트되었습니다.", null);
    }

    @ApiOperation(value= "판매자의 판매 물품 삭제 ", notes = "판매자의 판매 물품 삭제")
    @DeleteMapping("/seller/products/{product_id}")
    public ResponseDto<Object> deleteProduct(@PathVariable("product_id") Integer productId,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) throws ParseException {

        Integer memberId = userDetails.getMemberIdx();
        productService.deleteProduct(productId, memberId);

        return new ResponseDto<>(true, "판매자의 판매 물품이 정상적으로 삭제되었습니다.", null);
    }




    @ApiOperation("물품 조회 - 정렬 기준 설정 가능")
    @GetMapping("/products")
    public ResponseDto<?> findAllProducts(@RequestParam(value = "order", required = false) String order, Pageable pageable) throws ParseException {
        Page<ProductResponse> products = productService.findProductsPagination(order, pageable);
        return ResponseDto.success(products);
    }

    @ApiOperation("카테고리별 물품 조회")
    @GetMapping("/products/category/{category_idx}")
    public ResponseDto<?> findProductsByCategory(@RequestParam(value = "order", required = false) String order, @PathVariable("category_idx") Integer categoryIdx, Pageable pageable){
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

}
