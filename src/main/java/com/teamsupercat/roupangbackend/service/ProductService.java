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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

        Page<Product> productEntities;

        //예외처리: 우리의 정렬 기준이 아닐 때
        if(!(order.equals("priceAsc") || order.equals("priceDesc") || order.equals("") || order.equals("sales"))){
            throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
        }

        if (order.equals("priceAsc")) { //가격 오름차순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPrice(false, 0, pageable);
        } else if (order.equals("priceDesc")) { //가격 내림차순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThanOrderByPriceDesc(false, 0, pageable);
        } else { //등록순
            productEntities = productRepository.findProductByIsDeletedAndStockGreaterThan(false, 0, pageable);
        }

        //인기순(판매량순)
        if(order.equals("sales")){
            List<Map<String, Object>> productSales = productRepository.findAllProductsBySingleOrder();
            List<ProductResponse> productResponseList = new ArrayList<>();
            for(Map<String, Object> productSale : productSales){
                //ProductResponse productResponse = new ProductResponse((Integer)productSale.get("idx"), (String)productSale.get("product_name"), (Long)productSale.get("price"), (Integer) productSale.get("stock"), (String) productSale.get("description"), (String) productSale.get("description_img"), (String) productSale.get("products_category_idx"), (String) productSale.get("product_img"), (String) productSale.get("sales_end_date"), null);

//                log.info("idx={}", Integer.parseInt(String.valueOf(productSale.get("idx"))));
//                log.info("productname={}",String.valueOf(productSale.get("product_name")));
//                log.info("price={}", Long.parseLong(String.valueOf(productSale.get("price"))));
//                log.info("stock={}", Integer.parseInt(String.valueOf(productSale.get("stock"))));
//                log.info("description={}", String.valueOf(productSale.get("description")));
//                log.info("descimg={}",String.valueOf(productSale.get("description_img")));
//                log.info("category={}",String.valueOf(productSale.get("products_category_idx")));
//                log.info("productImg={}", String.valueOf(productSale.get("product_img")));
//                log.info("salesenddate={}",String.valueOf(productSale.get("sales_end_date")));
                ProductResponse productResponse = new ProductResponse(Integer.parseInt(String.valueOf(productSale.get("idx"))), String.valueOf(productSale.get("product_name")), Long.parseLong(String.valueOf(productSale.get("price"))), Integer.parseInt(String.valueOf(productSale.get("stock"))), String.valueOf(productSale.get("description")), String.valueOf(productSale.get("description_img")), String.valueOf(productSale.get("products_category_idx")), String.valueOf(productSale.get("product_img")), String.valueOf(productSale.get("sales_end_date")), new ArrayList<>());
                log.info(productResponse.toString());
                productResponseList.add(productResponse);
            }

            int page = pageable.getPageNumber();  //페이지 수
            int size = pageable.getPageSize();  //범위
            PageRequest pageRequest = PageRequest.of(page,size);
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()),productResponseList.size());

            //페이지를 초과해서 요청했을 때
            if((page+1)*size>productResponseList.size()){
                return new PageImpl<>(new ArrayList<>(), pageRequest, productResponseList.size());
            }

            return new PageImpl<>(productResponseList.subList(start,end), pageRequest, productResponseList.size());
        }

        //예외처리: 물품이 아무것도 없을 때
//        if(productRepository.findAll().isEmpty()){
//            throw new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND);
//        }

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

        if (order.equals("priceAsc")) { //가격 오름차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPrice(categoryIdx, pageable);
        } else if (order.equals("priceDesc")) { //가격 내림차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPriceDesc(categoryIdx, pageable);
        } else { //등록순
            productEntities = productRepository.findProductByProductsCategoryIdxId(categoryIdx, pageable);
        }

        //인기순(판매량순)
        if(order.equals("sales")){
            List<Map<String, Object>> productSales = productRepository.findProductsBySingleOrderAndCategoryIdx(categoryIdx);
            List<ProductResponse> productResponseList = new ArrayList<>();
            for(Map<String, Object> productSale : productSales){
                //ProductResponse productResponse = new ProductResponse((Integer)productSale.get("idx"), String.valueOf(productSale.get("product_name")), (Long)productSale.get("price"), (Integer) productSale.get("stock"), String.valueOf(productSale.get("description")), String.valueOf(productSale.get("description_img")), String.valueOf(productSale.get("products_category_idx")), String.valueOf(productSale.get("product_img")), String.valueOf(productSale.get("sales_end_date")), null);
                ProductResponse productResponse = new ProductResponse(Integer.parseInt(String.valueOf(productSale.get("idx"))), String.valueOf(productSale.get("product_name")), Long.parseLong(String.valueOf(productSale.get("price"))), Integer.parseInt(String.valueOf(productSale.get("stock"))), String.valueOf(productSale.get("description")), String.valueOf(productSale.get("description_img")), String.valueOf(productSale.get("products_category_idx")), String.valueOf(productSale.get("product_img")), String.valueOf(productSale.get("sales_end_date")), new ArrayList<>());
                productResponseList.add(productResponse);
            }

            int page = pageable.getPageNumber();  //페이지 수
            int size = pageable.getPageSize();  //범위
            PageRequest pageRequest = PageRequest.of(page,size);
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()),productResponseList.size());

            //페이지를 초과해서 요청했을 때
            if((page+1)*size>productResponseList.size()){
                return new PageImpl<>(new ArrayList<>(), pageRequest, productResponseList.size());
            }
            return new PageImpl<>(productResponseList.subList(start,end), pageRequest, productResponseList.size());
        }

        //예외처리: 카테고리에 상품이 없을 때
//        if(productRepository.findProductByProductsCategoryIdxId(categoryIdx).isEmpty()){
//            throw new CustomException(ErrorCode.SHOP_CATEGORY_PRODUCT_EMPTY_LIST);
//        }

        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    //키워드로 물품 검색
    //@Cacheable(value = "#keyword.concat(:).concat(#order)")
    public Page<ProductResponse> searchProduct(String keyword, String order, Pageable pageable){
        Page<Product> productEntities;

        //예외처리: 우리의 정렬 기준이 아닐 때
        if(!(order.equals("priceAsc") || order.equals("priceDesc") || order.equals("") || order.equals("sales"))){
            throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
        }

        if(order.equals("priceAsc")){ //가격 오름차순
            productEntities = productRepository.findProductByProductNameContainingOrderByPrice(keyword, pageable);
        }
        else if(order.equals("priceDesc")){ //가격 내림차순
            productEntities = productRepository.findProductByProductNameContainingOrderByPriceDesc(keyword, pageable);
        }
        else{ //등록순
            productEntities = productRepository.findProductByProductNameContaining(keyword, pageable);
        }

        //인기순(판매량순)
        if(order.equals("sales")){
            //List<Map<String, Object>> productSales = productRepository.findProductsBySingleOrderAndKeyword(keyword);
            List<Map<String, Object>> productSales = productRepository.findAllProductsBySingleOrder();

            productSales = productSales.stream().filter(p->String.valueOf(p.get("product_name")).contains(keyword)).collect(Collectors.toList());

            List<ProductResponse> productResponseList = new ArrayList<>();
            for(Map<String, Object> productSale : productSales){
                //ProductResponse productResponse = new ProductResponse((Integer)productSale.get("idx"), (String)productSale.get("product_name"), (Long)productSale.get("price"), (Integer) productSale.get("stock"), (String) productSale.get("description"), (String) productSale.get("description_img"), (String) productSale.get("products_category_idx"), (String) productSale.get("product_img"), (String) productSale.get("sales_end_date"), null);
                //ProductResponse productResponse = new ProductResponse((Integer)productSale.get("idx"), String.valueOf(productSale.get("product_name")), (Long)productSale.get("price"), (Integer) productSale.get("stock"), String.valueOf(productSale.get("description")), String.valueOf(productSale.get("description_img")), String.valueOf(productSale.get("products_category_idx")), String.valueOf(productSale.get("product_img")), String.valueOf(productSale.get("sales_end_date")), null);
                ProductResponse productResponse = new ProductResponse(Integer.parseInt(String.valueOf(productSale.get("idx"))), String.valueOf(productSale.get("product_name")), Long.parseLong(String.valueOf(productSale.get("price"))), Integer.parseInt(String.valueOf(productSale.get("stock"))), String.valueOf(productSale.get("description")), String.valueOf(productSale.get("description_img")), String.valueOf(productSale.get("products_category_idx")), String.valueOf(productSale.get("product_img")), String.valueOf(productSale.get("sales_end_date")), new ArrayList<>());
                productResponseList.add(productResponse);
            }

            int page = pageable.getPageNumber();  //페이지 수
            int size = pageable.getPageSize();  //범위
            PageRequest pageRequest = PageRequest.of(page,size);
            int start = (int) pageRequest.getOffset();
            int end = Math.min((start + pageRequest.getPageSize()),productResponseList.size());

            //페이지를 초과해서 요청했을 때
            if((page+1)*size>productResponseList.size()){
                return new PageImpl<>(new ArrayList<>(), pageRequest, productResponseList.size());
            }
            return new PageImpl<>(productResponseList.subList(start,end), pageRequest, productResponseList.size());
        }

        //예외처리: 해당 키워드를 포함하는 물품이 없을 때
//        if(productRepository.findProductByProductNameContaining(keyword).isEmpty()){
//            throw new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND);
//        }

        return productEntities.map(product -> new ProductResponse().toDto(product));
    }

    public List<Map<String, Object>> getProductSales(Pageable pageable) {
        List<Map<String, Object>> productAndTotalSales = productRepository.findAllProductsBySingleOrder();

        //A TupleBackedMap cannot be modified.
//        for(Map<String, Object> prozduct : productAndTotalSales){
//            String salesEndDate = product.get("sales_end_date").toString();
//            if(salesEndDate==null){
//                product.put("sales_end_date", "판매 종료 날짜가 없습니다.");
//            }
//            else{
//                product.put("sales_end_date", salesEndDate.substring(0,10));
//            }
//        }

        return productAndTotalSales;
    }
}

