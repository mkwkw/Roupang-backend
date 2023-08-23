package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.option.request.OptionWithProductRegisterRequest;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepository sellerRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
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

            //판매자 등록 여부
            Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

            //판매자로 등록되어 있으면
            if (Boolean.TRUE.equals(areYouSeller)) {
                throw new CustomException(ErrorCode.SELLER_ALREADY_EXISTS);
            } else {

            //판매자 생성(판매자 등록)
            Seller newSeller = Seller.builder()
                    .memberIdx(member)
                    .isDeleted(false)
                    .build();

            //판매자의 멤버 아이디 = 멤버의 아이디
            newSeller.getMemberIdx().setId(member.getId());

            sellerRepository.save(newSeller);

            return newSeller.getId();
           }
        } else throw new CustomException(ErrorCode.SELLER_USER_NOT_FOUND);
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
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_USER_NOT_FOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        //판매자로 등록되어 있으면
        if (Boolean.TRUE.equals(areYouSeller)) {
            //판매자 찾기
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            //product 엔티티로 변환
            Product product = productCreateRequest.toEntity(productCreateRequest, sellerFound);

            //product 엔티티 저장
            Product savedProduct = productRepository.save(product);

            //판매 물품 옵션
            List<OptionWithProductRegisterRequest> optionWithProductRegisterRequests = productCreateRequest.getOptions();

            if (!productCreateRequest.getExistsOption()) {
                productRepository.save(savedProduct);

            } else {
                insertProductOptions(savedProduct, optionWithProductRegisterRequests);
            }
        } else {
            //에러: 판매자만 물품 등록 할 수 있습니다.
            throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);
        }
    }

    //todo 3. 판매자의 판매 물품 전체 조회

    /* page: 현재 페이지, 0부터 시작한다.
       size: 한 페이지에 노출할 데이터 건수 */
    public Page<AllProductsResponse> getProductsList(String order, Pageable pageable, Integer memberId) {
        //멤버 찾기 없으면 에러
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_USER_NOT_FOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        Page<AllProductsResponse> allProductsResponses;

        //판매자로 등록되어 있으면
        if (Boolean.TRUE.equals(areYouSeller)) {

            //판매자 불러오기: 판매자 레포에서 멤버를 넣고 판매자를 찾는다.
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            //판매자의 물품 리스트 불러오기: 판매자 id 넣고 "isDeleted = false"인 것들만.
            Page<Product> productList = productRepository.findAllByIsDeletedAndSellerIdx(false, sellerFound, pageable);

            if (productList.isEmpty()) {
                //판매자로 등록했지만 파는 물품이 없는 경우
//              throw new CustomException(ErrorCode.SELLER_PRODUCT_EMPTY_LIST);
//              return null;
                allProductsResponses = productList.map(product -> AllProductsResponse.fromProduct(product));
            } else {

                // "order" 파라미터를 기본값으로 설정
                String defaultOrder = "createdAtDesc";

                // "order" 파라미터가 없거나 비어있을 경우 기본값으로 설정
                if (order == null || order.isEmpty()) {
                    order = defaultOrder;
                }
                // 기본 정렬 방식에 따라 처리
                if (order.equals("priceAsc")) {
                    productList = productRepository.findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByPrice(false, 0, sellerFound, pageable);
                } else if (order.equals("priceDesc")) {
                    productList = productRepository.findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByPriceDesc(false, 0, sellerFound, pageable);
                } else if (order.equals("sales")) {
                    productList = productRepository.findBySellerOrderBySalesAmounts(sellerFound, pageable);
                } else if (order.equals("createdAtDesc")) {
                    productList = productRepository.findProductByIsDeletedAndStockGreaterThanEqualAndSellerIdxOrderByCreatedAtDesc(false, 0, sellerFound, pageable);
                } else {
                    throw new CustomException(ErrorCode.SHOP_BAD_SORT_REQUEST);
                }


                //Entity -> Dto로 변환
                allProductsResponses = productList.map(product -> AllProductsResponse.fromProduct(product));
            }
        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);

//      List<AllProductsResponse> contentList = allProductsResponses.getContent();

        return allProductsResponses;
    }

    //todo 5. 판매자의 판매 물품 수정
    @Transactional
    public void updateProduct(Integer productId, Integer memberId, ProductCreateRequest productCreateRequest) throws ParseException {

        //유저 찾기, 없으면 에러
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_USER_NOT_FOUND));

        //판매하고 있는 product 찾기, 없으면 에러
        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_PRODUCT_NOT_FOUND));

        //유저의 판매자 여부 확인
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        //판매자라면
        if (Boolean.TRUE.equals(areYouSeller)) {
            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            //자신이 판매하는 물품인지 여부 확인
            if (existingProduct.getSellerIdx().getId() == sellerFound.getId()) {

                productCreateRequest.updateEntity(existingProduct, sellerFound, productId);

                Product savedProduct = productRepository.save(existingProduct);

                //option: 삭제 후 재등록
                List<OptionWithProductRegisterRequest> optionWithProductRegisterRequests = productCreateRequest.getOptions();

                deleteProductOptions(productId);
                insertProductOptions(savedProduct, optionWithProductRegisterRequests);

            } else throw new CustomException(ErrorCode.SHOP_MISMATCH_SELLER);

        //판매자가 아니면 에러
        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);
    }

    //todo 6. 판매자의 판매 물품 삭제
    @Transactional
    public void deleteProduct(Integer productId, Integer memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_USER_NOT_FOUND));

        Product existingProduct = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.SELLER_PRODUCT_NOT_FOUND));

        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        if (Boolean.TRUE.equals(areYouSeller)) {

            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            if (existingProduct.getSellerIdx().getId() == sellerFound.getId()) {

                List<OptionType> optionTypes = optionTypeRepository.findAllByProductIdx(productId);
                List<OptionDetail> optionDetails = optionDetailRepository.findAllByProductIdx(existingProduct);

                if (optionTypes == null && optionDetails == null) {
                    productRepository.deleteProduct(productId);

                } else {
                    productRepository.deleteProduct(productId);
                    optionTypeRepository.deleteOptionType(productId);
                    optionDetailRepository.deleteOptionDetail(existingProduct);

                }
            } else throw new CustomException(ErrorCode.SHOP_MISMATCH_SELLER);
        } else throw new CustomException(ErrorCode.SHOP_PRODUCT_ONLY_SELLERS);
    }

    //todo 물품 옵션 등록 메소드
    public void insertProductOptions(Product savedProduct, List<OptionWithProductRegisterRequest> optionRegisterRequests) {
        for (OptionWithProductRegisterRequest optionWithProductRegisterRequest : optionRegisterRequests) {

            // OptionService의 registerOptionOfProduct 메소드를 호출하여 옵션 등록
            optionService.registerOptionOfProduct(optionWithProductRegisterRequest, savedProduct);
        }
        productRepository.save(savedProduct);
    }


    //todo 물품 옵션 삭제 메소드
    public void deleteProductOptions(Integer productId) {
        List<OptionType> optionTypes = optionTypeRepository.findAllByProductIdx(productId);

//        for (OptionType optionType : optionTypes) {
//            optionDetailRepository.deleteAllByOptionTypeIdx(optionType.getId());}
//        optionTypeRepository.deleteAllByProductIdx(productId);

        //OptionType의 Id만 추출해서 리스트로 반환
        optionTypes.stream().map(OptionType::getId).forEach(optionTypeId -> {
            optionDetailRepository.deleteAllByOptionTypeIdx(optionTypeId);
            optionTypeRepository.deleteById(optionTypeId);
        });
    }

}