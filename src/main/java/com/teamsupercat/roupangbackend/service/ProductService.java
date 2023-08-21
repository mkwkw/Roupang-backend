package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.dto.option.request.OptionRegisterRequest1;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.entity.*;
import com.teamsupercat.roupangbackend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;


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

    //todo 1. 판매자 등록
    public Integer saveAsSeller(Integer memberId) {

        //memberId를 넣고 멤버 찾기 없으면 에러
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        //멤버 아이디가 존재하면
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            //판매자 생성(판매자 등록)
            Seller newSeller = Seller.builder()
                    .memberIdx(member)
                    .isDeleted(false)
                    .build();

            //판매자의 멤버 아이디 = 멤버의 아이디
            newSeller.getMemberIdx().setId(member.getId());

            sellerRepository.save(newSeller);

            return newSeller.getId();

        } else throw new CustomException(ErrorCode.SHOP_USER_NOT_FOUND);

    }

    //todo 2. 판매 물품 등록
    @Transactional
    public void createProduct(ProductCreateRequest productCreateRequest, Integer memberId) throws IOException, ParseException {

        /*
        1. 멤버가 아닌 경우 NO -> 에러
        2. 멤버지만 판매자 등록을 하지 않은 경우 (member 아이디를 가지고 Seller 뒤지는 데 해당 member.id가 발견되지 않은 경우) NO -> 에러
        3. 멤버이자 판매자인 경우 OK
        */

        //멤버 찾기 없으면 에러
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SHOP_USER_NOT_FOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        //판매자로 등록되어 있으면
        if (Boolean.TRUE.equals(areYouSeller)) {
            //판매자 찾기
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            //판매 물품
            Product product = productCreateRequest.toEntity(productCreateRequest, sellerFound);

            //product 엔티티 저장
            Product savedProduct = productRepository.save(product);

            //판매 물품 옵션
            //List<OptionTypeRequest> 정의
            List<OptionRegisterRequest1> optionRegisterRequests = productCreateRequest.getOptions();

            if(!productCreateRequest.getExistsOption()) {
                productRepository.save(savedProduct);

            } else {
                insertProductOptions(savedProduct, optionRegisterRequests);
            }
        } else {
            throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS); //에러: 판매자만 물품 등록 할 수 있습니다.
        }
    }


    //todo 3. 판매 물품 상세 조회(판매자, 구매자인 경우 모두 동일)
    @Transactional
    public ProductResponse getProductOne(Integer productId) throws ParseException {

        //product 찾기, 없으면 에러
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

//      //product 찾기
//      Product product = productRepository.findProductByIsDeletedAndId(false, productId);

        //product 카테고리 찾기, 없으면 에러(영속화)
        ProductsCategory productsCategory = productsCategoryRepository.findById(product.getProductsCategoryIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_CATEGORY_NOT_FOUND));

        //singerOrder 개별 주문 결제건 리스트 찾기
        List<SingleOrder> singleOrders = singleOrderRepository.findByProductIdx(product);

        //product의 재고에서 개별 결제건들의 amount들을 빼서 product 재고의 남은 양을 확인한다.
        Integer totalSoldAmount = singleOrders.stream().mapToInt(SingleOrder::getAmount).sum();

        Integer remainingStock = product.getStock() - totalSoldAmount;

        //product의 재고를 수정
        product.setStock(remainingStock);

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

    /* <멤버>
     * 그냥 멤버이고 판매자가 아닌 경우 -> NO
     * 그냥 멤버도 아닌 경우 -> NO
     * 판매자인 경우 -> YES
     *
     * <물품 리스트>
     * isDeleted = false 인 것만 불러온다
     * 물품 리스트 비어있으면 return null
     *
     * <정렬>
     * 물품 리스트가 없으면 return null
     *
     * <카테고리>
     * 물품의 카테고리가 비어있으면 전체 다 보여준다.
     * 물품의 카테고리가 있으면 해당 카테고리를 가진 물품 리스트만 보여준다.
     *
     */

    //todo 4. 판매자의 판매 물품 전체 조회

    /* page: 현재 페이지, 0부터 시작한다.
       size: 한 페이지에 노출할 데이터 건수 */
    public List<AllProductsResponse> getProductsList(String order, Pageable pageable, Integer memberId) {
        //멤버 찾기 없으면 에러
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SHOP_USER_NOT_FOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        Page<AllProductsResponse> allProductsResponses;

        //판매자로 등록되어 있으면
        if (Boolean.TRUE.equals(areYouSeller)) {

            //판매자 불러오기: 판매자 레포에서 멤버를 넣고 판매자를 찾는다.
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            //판매자의 물품 리스트(엔티티)를 불러오기: 판매자 id 넣고 isDeleted = false인 것들만.
            Page<Product> productList = productRepository.findAllByIsDeletedAndSellerIdx(false, sellerFound, pageable);

            if (productList.isEmpty()) {
                //판매자로 등록했지만 파는 물품이 없는 경우, 에러
                throw new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND);
            } else {

                //높은 가격순: 파마미터로 그냥 pageable만 넣으면 전체조회가 되어버린다. 판매자 기준으로 필터링한 상태에서 정렬하고 싶으면 Seller도 파라미터로 넣어줘야 한다.
                if (order.equals("낮은가격")) {
                    productList = productRepository.findBySellerIdxOrderByPrice(sellerFound, pageable);
                }
                //낮은 가격순
                else if (order.equals("높은가격")) {
                    productList = productRepository.findBySellerIdxOrderByPriceDesc(sellerFound, pageable);
                }
                //최신순
                else if (order.equals("신상품")) {
                    productList = productRepository.findBySellerIdxOrderByCreatedAtDesc(sellerFound, pageable);
                }
                //판매순
                else if (order.equals("판매순")) {
                    productList = productRepository.findBySellerOrderBySalesAmounts(sellerFound, pageable);
                }

                //Entity -> Dto로 변환
                allProductsResponses = productList.map(product -> AllProductsResponse.fromProduct(product));
            }
        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);

        List<AllProductsResponse> contentList = allProductsResponses.getContent();

        return contentList;
    }

    //todo 5. 판매자의 판매 물품 수정
    @Transactional
    public void updateProduct(Integer productId, Integer memberId, ProductCreateRequest productCreateRequest) throws ParseException {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SHOP_USER_NOT_FOUND));

        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        if (Boolean.TRUE.equals(areYouSeller)) {
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);
            if (existingProduct.getSellerIdx().getId() == sellerFound.getId()) {

                productCreateRequest.updateEntity(existingProduct, sellerFound, productId);

                Product savedProduct = productRepository.save(existingProduct);

                //option: 삭제 후 재등록
                deleteProductOptions(productId);
                List<OptionRegisterRequest1> optionRegisterRequests = productCreateRequest.getOptions();
                insertProductOptions(savedProduct, optionRegisterRequests);

            } else throw new CustomException(ErrorCode.SHOP_MISMATCH_SELLER);

        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);

    }
    //todo 6. 판매자의 판매 물품 삭제
    @Transactional
    public void deleteProduct(Integer productId, Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SHOP_USER_NOT_FOUND));

        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.CART_PRODUCT_NOT_FOUND));

        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        if (Boolean.TRUE.equals(areYouSeller)) {
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);
            if (existingProduct.getSellerIdx().getId() == sellerFound.getId()) {
                List<OptionType> optionTypes = optionTypeRepository.findAllByProductIdx(productId);
                List<OptionDetail> optionDetails = optionDetailRepository.findAllByProductIdx(existingProduct);
                if(optionTypes == null && optionDetails == null) {
                    productRepository.deleteById(productId);
                } else{
                deleteProductOptions(productId);
                productRepository.deleteById(productId);}

            } else throw new CustomException(ErrorCode.SHOP_MISMATCH_SELLER);
        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);
    }

    //todo 물품 옵션 삭제 메소드
    public void deleteProductOptions(Integer productId) {
        List<OptionType> optionTypes = optionTypeRepository.findAllByProductIdx(productId);
//        for (OptionType optionType : optionTypes) {
//            optionDetailRepository.deleteAllByOptionTypeIdx(optionType.getId());
//        }
//        optionTypeRepository.deleteAllByProductIdx(productId);
            //OptionType의 Id만 추출해서 리스트로 반환
            optionTypes.stream().map(OptionType::getId).forEach(optionTypeId -> {
                optionDetailRepository.deleteAllByOptionTypeIdx(optionTypeId);
                optionTypeRepository.deleteById(optionTypeId);
            });

    }

    //todo 물품 옵션 등록 메소드
    public void insertProductOptions(Product savedProduct, List<OptionRegisterRequest1> optionRegisterRequests) {
        for (OptionRegisterRequest1 optionRegisterRequest : optionRegisterRequests) {
            // OptionService의 registerOptionOfProduct 메소드를 호출하여 옵션 등록
            optionService.registerOptionOfProduct(optionRegisterRequest, savedProduct);
        }
        productRepository.save(savedProduct);
    }


    //물품 전체 조회
    public Page<ProductResponse> findProductsPagination(String order, Pageable pageable) {

        Page<Product> productEntities;

        if (order.equals("priceAsc")) { //가격 오름차순
            productEntities = productRepository.findProductByIsDeletedAndPriceGreaterThanOrderByPrice(false, 0L, pageable);
        } else if (order.equals("priceDesc")) { //가격 내림차순
            productEntities = productRepository.findProductByIsDeletedAndPriceGreaterThanOrderByPriceDesc(false, 0L, pageable);
        } else { //등록순
            productEntities = productRepository.findProductByIsDeletedAndPriceGreaterThan(false, 0L, pageable);
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

