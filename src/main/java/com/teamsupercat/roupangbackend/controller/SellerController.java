package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.CustomUserDetail.CustomUserDetail;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
import com.teamsupercat.roupangbackend.service.OptionService;
import com.teamsupercat.roupangbackend.service.SellerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Api(tags = "판매자 API")
public class SellerController {

    private final OptionService optionService;
    private final SellerService sellerService;

    @ApiOperation(value= "판매자 등록", notes = "판매자로 등록하기")
    @PostMapping("/seller/signup")
    public ResponseDto<Object> resisterAsSeller(@AuthenticationPrincipal CustomUserDetail userDetails){

        Integer memberId = userDetails.getMemberIdx();

        sellerService.saveAsSeller(memberId);

        return new ResponseDto<>(true, "판매자로 등록되었습니다.", null);
    }

    @ApiOperation(value= "판매 물품 등록 ", notes = "판매할 물품 등록하기")
    @PostMapping("/seller/products/register")
    public ResponseDto<Object> createProduct(@RequestBody ProductCreateRequest productCreateRequest, @AuthenticationPrincipal CustomUserDetail userDetails) throws IOException, ParseException {

        Integer memberId = userDetails.getMemberIdx();

        sellerService.createProduct(productCreateRequest, memberId);

        return new ResponseDto<>(true, "물품 등록에 성공하였습니다.",null);
    }

    @ApiOperation(value= "판매자의 판매 물품 리스트 조회 ", notes = "판매자의 판매 물품 리스트 조회")
    @GetMapping("/seller/products")
    public ResponseDto<Object> getProductsList(
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetail userDetails){

        Integer memberId = userDetails.getMemberIdx();
        Pageable pageable = PageRequest.of(page, size); // 기본 값 설정

        Page<AllProductsResponse> productsList = sellerService.getProductsList(order, pageable, memberId);

        return new ResponseDto<>(true, "판매자의 판매 물품 내역 조회에 성공하였습니다.", productsList);
    }

    @ApiOperation(value= "판매자의 판매 물품 수정 ", notes = "판매자의 판매 물품 수정")
    @PatchMapping("/seller/products/{product_id}")
    public ResponseDto<Object> updateProduct(@PathVariable("product_id") Integer productId,
                                             @RequestBody ProductCreateRequest productCreateRequest,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) throws ParseException {

        Integer memberId = userDetails.getMemberIdx();

        sellerService.updateProduct(productId, memberId, productCreateRequest);

        return new ResponseDto<>(true, "판매자의 판매 물품이 정상적으로 업데이트되었습니다.", "Product ID: " + productId);
    }

    @ApiOperation(value= "판매자의 판매 물품 삭제 ", notes = "판매자의 판매 물품 삭제")
    @DeleteMapping("/seller/products/{product_id}")
    public ResponseDto<Object> deleteProduct(@PathVariable("product_id") Integer productId,
                                             @AuthenticationPrincipal CustomUserDetail userDetails) throws ParseException {

        Integer memberId = userDetails.getMemberIdx();
        sellerService.deleteProduct(productId, memberId);

        return new ResponseDto<>(true, "판매자의 판매 물품이 정상적으로 삭제되었습니다.", "Product ID: " + productId);
    }


}
