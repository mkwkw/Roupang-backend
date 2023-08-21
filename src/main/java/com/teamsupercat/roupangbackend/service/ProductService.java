package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import com.teamsupercat.roupangbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    private final ProductsCategoryRepository productsCategoryRepository;
    private final SingleOrderRepository singleOrderRepository;
    private final OptionService optionService;
    private final OptionTypeRepository optionTypeRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final OptionTypeNameRepository optionTypeNameRepository;



    //todo 4. 판매 물품 상세 조회(판매자, 구매자인 경우 모두 동일)
    public ProductResponse getProductOne(Integer productId) throws ParseException {

        //product 찾기, 없으면 에러
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

//      //product 찾기
//      Product product = productRepository.findProductByIsDeletedAndId(false, productId);

        //product 카테고리 찾기, 없으면 에러(영속화)
        ProductsCategory productsCategory = productsCategoryRepository.findById(product.getProductsCategoryIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_CATEGORY_NOT_FOUND));

        //optionService에서 options 불러오기
        Map<String, Object> options = optionService.findOptionByProductIdx(productId);

        List<OptionTypeResponse> optionTypeResponseList;

        //options Map에서 "options"의 key, value만 사용한다.
        if (options.containsKey("options")) {

            optionTypeResponseList = (List<OptionTypeResponse>) options.get("options");

        } else return null;

        ProductResponse productResponse = new ProductResponse();

        return productResponse.toDto2(product, optionTypeResponseList);

    }



    //물품 전체 조회
    public Page<ProductResponse> findProductsPagination(String order, Pageable pageable) {

        Page<Product> productEntities;

        if (order.equals("priceAsc")) { //가격 오름차순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPrice(false, 0, pageable);
        } else if (order.equals("priceDesc")) { //가격 내림차순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPriceDesc(false, 0, pageable);
        } else { //등록순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThan(false, 0, pageable);
        }

        //TODO. 인기순(판매량순)


        return productEntities.map(product -> new ProductResponse().toDto(product));
        //return productEntities.map(productMapper.INSTANCE::ProductEntityToProductResponse);
        //return productEntities;
    }

    public Page<ProductResponse> findProductsByCategoryIdxPagination(String order, Integer categoryIdx, Pageable pageable) {
        //TODO. 해당 카테고리에 해당하는 물품이 없는 경우 - 예외 처리
        Page<Product> productEntities;

        if (order.equals("priceAsc")) { //가격 오름차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPrice(categoryIdx, pageable);
        } else if (order.equals("priceDesc")) { //가격 내림차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPriceDesc(categoryIdx, pageable);
        } else { //등록순
            productEntities = productRepository.findProductByProductsCategoryIdxId(categoryIdx, pageable);
        }

        //TODO. 인기순(판매량순)


        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    public Page<ProductResponse> searchProduct(String keyword, String order, Pageable pageable){
        Page<Product> productEntities;

        if(order.equals("priceAsc")){ //가격 오름차순
            productEntities = productRepository.findProductByProductNameContainingOrderByPrice(keyword, pageable);
        }
        else if(order.equals("priceDesc")){ //가격 내림차순
            productEntities = productRepository.findProductByProductNameContainingOrderByPriceDesc(keyword, pageable);
        }
        else{ //등록순
            productEntities = productRepository.findProductByProductNameContaining(keyword, pageable);
        }

        //TODO. 예외처리
//        if(productEntities.isEmpty()){
//            throw new CustomException(ErrorCode);
//        }

        //TODO. 인기순(판매량순)


        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

}

