package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import com.teamsupercat.roupangbackend.repository.ProductsCategoryRepository;
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
    private final ProductsCategoryRepository productsCategoryRepository;
    private final OptionService optionService;

    //todo 4. 판매 물품 상세 조회(판매자, 구매자인 경우 모두 동일)
    public ProductResponse getProductOne(Integer productId) throws ParseException {

        //product 찾기, 없으면 예외
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        //isDeleted == false 인 product 만 보여줌
        if(product.getIsDeleted() == true){
            throw new CustomException(ErrorCode.SELLER_PRODUCT_NOT_FOUND);
        } else {

            //product 카테고리 찾기, 없으면 예외(영속화)
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

    }

    //물품 전체 조회
    //@Cacheable(value = "products", key = "#order")
    public Page<ProductResponse> findProductsPagination(String order, Pageable pageable) {

        //예외처리: 우리의 정렬 기준이 아닐 때
        if(!(order.equals("priceAsc") || order.equals("priceDesc") || order.equals("") || order.equals("sales"))){
            throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
        }

        Page<Product> productEntities;

        switch (order) {
            case "priceAsc":  //가격 오름차순
                productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPrice(false, 0, pageable);
                break;
            case "priceDesc":  //가격 내림차순
                productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPriceDesc(false, 0, pageable);
                break;
            case "sales":  //판매순
                productEntities = productRepository.findAllProductsOrderBySalesAmounts(pageable);
                break;
            default:  //등록순
                productEntities = productRepository.findProductByIsDeletedAndStockGreaterThan(false, 0, pageable);
                break;
        }

        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    //카테고리별 물품 조회
    //@Cacheable(value = "#categoryIdx.toString().concat(:).concat(#order)")
    public Page<ProductResponse> findProductsByCategoryIdxPagination(String order, Integer categoryIdx, Pageable pageable) {

        //예외처리: 우리가 갖고있는 카테고리가 아닐 때
        productsCategoryRepository.findById(categoryIdx).orElseThrow(()->new CustomException(ErrorCode.SHOP_CATEGORY_NOT_FOUND));

        //예외처리: 우리의 정렬 기준이 아닐 때
        if(!(order.equals("priceAsc") || order.equals("priceDesc") || order.equals("") || order.equals("sales"))){
            throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
        }

        Page<Product> productEntities;

        switch (order) {
            case "priceAsc":  //가격 오름차순
                productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPrice(categoryIdx, pageable);
                break;
            case "priceDesc":  //가격 내림차순
                productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPriceDesc(categoryIdx, pageable);
                break;
            case "sales": //판매순
                productEntities = productRepository.findProductsByCategoryIdxOrderBySalesAmounts(categoryIdx, pageable);
                break;
            default:  //등록순
                productEntities = productRepository.findProductByProductsCategoryIdxId(categoryIdx, pageable);
                break;
        }

        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    //키워드로 물품 검색
    //@Cacheable(value = "#keyword.concat(:).concat(#order)")
    public Page<ProductResponse> searchProduct(String keyword, String order, Pageable pageable){

        //예외처리: 우리의 정렬 기준이 아닐 때
        if(!(order.equals("priceAsc") || order.equals("priceDesc") || order.equals("") || order.equals("sales"))){
            throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
        }

        Page<Product> productEntities;

        switch (order) {
            case "priceAsc":  //가격 오름차순
                productEntities = productRepository.findProductByProductNameContainingOrderByPrice(keyword, pageable);
                break;
            case "priceDesc":  //가격 내림차순
                productEntities = productRepository.findProductByProductNameContainingOrderByPriceDesc(keyword, pageable);
                break;
            case "sales": // 판매순
                productEntities = productRepository.findProductsByKeywordOrderBySalesAmounts(keyword, pageable);
                break;
            default:  //등록순
                productEntities = productRepository.findProductByProductNameContaining(keyword, pageable);
                break;
        }

        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    //총 판매량 조회
    public List<Map<String, Object>> getProductSales(Pageable pageable) {
        List<Map<String, Object>> productAndTotalSales = productRepository.findAllProductsBySingleOrder();

        return productAndTotalSales;
    }
}

