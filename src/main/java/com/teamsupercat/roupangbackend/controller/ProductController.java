package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.dto.seller.SellerRequest;
import com.teamsupercat.roupangbackend.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/seller")
@Api(tags = "물품 API")
public class ProductController {

    private final ProductService productService;

    @ApiOperation(value= "판매자 등록", notes = "판매자로 등록하기")
    @PostMapping("/signup")
    public ResponseDto<Object> resisterAsSeller(@RequestBody SellerRequest sellerRequest){

//       sellerRequest.setId(1);
//       Integer sellerId = productService.saveAsSeller(1);

        Integer sellerId = productService.saveAsSeller(sellerRequest);


       return new ResponseDto<>(true, "판매자로 등록되었습니다.", null);
    }


    @ApiOperation(value= "판매 물품 등록 ", notes = "판매할 물품 등록하기")
    @PostMapping("/products/register")
    public ResponseDto<Object> createProduct(@RequestBody ProductCreateRequest productCreateRequest) throws IOException, ParseException {

        Integer userId = 1;

        productService.createProduct(productCreateRequest, userId);

        return new ResponseDto<>(true, "물품 등록에 성공하였습니다.",null);

    }

    @ApiOperation(value= "판매 물품 상세 조회 ", notes = "물품 상세 조회")
    @GetMapping("/products/{product_id}")
    public ResponseDto<Object> getProductOne(@PathVariable("product_id") Integer productId) throws ParseException {

        ProductResponse productResponse = productService.getProductOne(productId);

        return new ResponseDto<>(true, "물품 조회에 성공하였습니다.", productResponse);

    }

//    @ApiOperation(value= "판매 물품들 내역 조회 ", notes = "판매자의 판매 물품 리스트 조회")
//    @GetMapping("/seller/products")
//    public ResponseDto<Object> getProductsList(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(required = false) String order,
//            @RequestParam(required = false) String category){
//
//        Integer userId = 1;
//
//        List<AllProductsResponse> allProductsResponseList = productService.getProductsList(page, size, order, category, userId);
//
//
//        return new ResponseDto<>(true, "판매자의 판매 물품 내역 조회에 성공하였습니다.", allProductsResponseList);
//
//    }



}
