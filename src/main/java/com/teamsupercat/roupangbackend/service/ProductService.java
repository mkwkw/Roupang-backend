package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.dto.seller.SellerRequest;
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
import java.util.HashMap;
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

    //todo 1. 판매자 등록
    public Integer saveAsSeller(SellerRequest sellerRequest) {

        //요청의 id를 넣고 멤버 찾기 없으면 에러
        Optional<Member> optionalMember = memberRepository.findById(sellerRequest.getId());

        //멤버의 아이디가 존재하면
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

        } else throw new CustomException(ErrorCode.USER_NOTFOUND);

    }

    //todo 2. 판매 물품 등록
    public void createProduct(ProductCreateRequest productCreateRequest, Integer userId) throws IOException, ParseException {

        /*
        1. 멤버가 아닌 경우 NO -> 에러
        2. 멤버지만 판매자 등록을 하지 않은 경우 (member 아이디를 가지고 Seller 뒤지는 데 해당 member.id가 발견되지 않은 경우) NO -> 에러
        3. 멤버이자 판매자인 경우 OK
        */

        //멤버 찾기 없으면 에러
        Member member = memberRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOTFOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        //판매자로 등록되어 있으면
        if (Boolean.TRUE.equals(areYouSeller)) {

            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

            Product product = productCreateRequest.toEntity(productCreateRequest, sellerFound);

            productRepository.save(product);

        } else throw new CustomException(ErrorCode.SELLER_ONLY);

    }

    //todo 3. 판매자의 판매 물품 상세 조회(구매자의 그냥 물품 상세 조회)
    @Transactional
    public ProductResponse getProductOne(Integer productId) throws ParseException {

        //product 찾기, 없으면 에러
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOTFOUND));

        //product 카테고리 찾기, 없으면 에러(영속화)
        ProductsCategory productsCategory = productsCategoryRepository.findById(product.getProductsCategoryIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOTFOUND));

        //singerOrder 개별 주문 결제건 리스트 찾기
        List<SingleOrder> singleOrders = singleOrderRepository.findByProductIdx(product);

        //product의 재고에서 개별 결제건들의 amount들을 빼서 product 재고의 남은 양을 확인한다.
        Integer totalSoldAmount = singleOrders.stream().mapToInt(SingleOrder::getAmount).sum();
        Integer remainingStock = product.getStock() - totalSoldAmount;

        //product의 재고를 수정
        product.setStock(remainingStock);

        ProductResponse productResponse = new ProductResponse();

        return productResponse.toDto(product);

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

    public  List<AllProductsResponse> getProductsList(String order, Pageable pageable, Integer userId) {
        //멤버 찾기 없으면 에러
        Member member = memberRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOTFOUND));

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
                throw new CustomException(ErrorCode.NOTFOUND_PRODUCT);
            } else {

                //높은 가격순
                if (order.equals("낮은가격")) {
                    productList = productRepository.findProductByOrderByPrice(pageable);
                }
                //낮은 가격순
                else if (order.equals("높은가격")) {
                    productList = productRepository.findProductByOrderByPriceDesc(pageable);
                }
                //최신순
                else if (order.equals("신상품")){
                    productList = productRepository.findProductsByOrderByCreatedAtDesc(pageable);
                }
                //판매순




                //Entity -> Dto로 변환
                allProductsResponses = productList.map(product -> AllProductsResponse.fromProduct(product));

                //allProductsResponses = productList.map(product -> new AllProductsResponse());

            }

        } else throw new CustomException(ErrorCode.SELLER_ONLY);

       List<AllProductsResponse> contentList = allProductsResponses.getContent();

        return contentList;
    }




    //물품 전체 조회
    public Page<ProductResponse> findProductsPagination(String order, Pageable pageable){

        Page<Product> productEntities;

        if(order.equals("priceAsc")){ //가격 오름차순
            productEntities = productRepository.findProductByOrderByPrice(pageable);
        }
        else if(order.equals("priceDesc")){ //가격 내림차순
            productEntities = productRepository.findProductByOrderByPriceDesc(pageable);
        }
        else{ //등록순
            productEntities = productRepository.findAll(pageable);
        }

        //TODO. 인기순(판매량순)


        return productEntities.map(product -> {
            try {
                return new ProductResponse().toDto(product);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new ProductResponse();
        });
        //return productEntities.map(productMapper.INSTANCE::ProductEntityToProductResponse);
        //return productEntities;
    }

    public Page<ProductResponse> findProductsByCategoryIdxPagination(String order, Integer categoryIdx, Pageable pageable) {
        //TODO. 해당 카테고리에 해당하는 물품이 없는 경우 - 예외 처리
        Page<Product> productEntities;

        if(order.equals("priceAsc")){ //가격 오름차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPrice(categoryIdx, pageable);
        }
        else if(order.equals("priceDesc")){ //가격 내림차순
            productEntities = productRepository.findProductByProductsCategoryIdxIdOrderByPriceDesc(categoryIdx, pageable);
        }
        else{ //등록순
            productEntities = productRepository.findProductByProductsCategoryIdxId(categoryIdx, pageable);
        }

        //TODO. 인기순(판매량순)


        return productEntities.map(product -> {
            try {
                return new ProductResponse().toDto(product);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return new ProductResponse();
        });
    }

    public Map<String, Object> findProductsByOption(List<String> options, Pageable pageable) {
        //옵션디테일 레포지토리에서 options에 해당하는 상품을 각각 찾아서 교집합 찾기
        return new HashMap<>();
    }



}

