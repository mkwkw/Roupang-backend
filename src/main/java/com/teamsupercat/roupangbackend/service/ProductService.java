package com.teamsupercat.roupangbackend.service;


import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.product.AllProductsResponse;
import com.teamsupercat.roupangbackend.dto.product.ProductCreateRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.dto.seller.SellerRequest;
import com.teamsupercat.roupangbackend.entity.Member;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.entity.ProductsCategory;
import com.teamsupercat.roupangbackend.entity.Seller;
import com.teamsupercat.roupangbackend.repository.MemberRepository;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import com.teamsupercat.roupangbackend.repository.ProductsCategoryRepository;
import com.teamsupercat.roupangbackend.repository.SellerRepository;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;

import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
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
        Member member= memberRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOTFOUND));

        //판매자 등록 여부
        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);

        //판매자로 등록되어 있으면
        if(Boolean.TRUE.equals(areYouSeller)) {

        Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);

        Product product = productCreateRequest.toEntity(productCreateRequest,sellerFound);

        productRepository.save(product);

        } else throw new CustomException(ErrorCode.SELLER_ONLY);

    }

    //todo 3. 판매자의 판매 물품 상세 조회(구매자의 그냥 물품 상세 조회)
    public ProductResponse getProductOne(Integer productId) throws ParseException {

        //product 찾기, 없으면 에러
        Product product = productRepository.findById(productId).orElseThrow(()-> new CustomException(ErrorCode.PRODUCT_NOTFOUND));

        //product 카테고리 찾기, 없으면 에러(영속화)
        ProductsCategory productsCategory = productsCategoryRepository.findById(product.getProductsCategoryIdx().getId()).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOTFOUND));


        ProductResponse productResponse = new ProductResponse();

        return productResponse.toDto(product);


    }

//    //todo 4. 판매자의 판매 물품 전체 조회
//    public List<AllProductsResponse> getProductsList(int page, int size, String order, String category, Integer userId) {
//
//        Pageable pageable;
//
//        /* <멤버>
//        * 그냥 멤버이고 판매자가 아닌 경우 -> NO
//        * 그냥 멤버도 아닌 경우 -> NO
//        * 판매자인 경우 -> YES
//        *
//        * <물품 리스트>
//        * isDeleted = false 인 것만 불러온다
//        * 물품 리스트 비어있으면 return null
//        *
//        * <페이지 번호> 이상하게 입력하면 에러
//        * <사이즈> 이상하게 입력시 에러
//        *
//        * <정렬>
//        * 물품 리스트가 없으면 return null
//        *
//        * <카테고리>
//        * 물품의 카테고리가 비어있으면 전체 다 보여준다.
//        * 물품의 카테고리가 있으면 해당 카테고리를 가진 물품 리스트만 보여준다.
//        *
//        */
//
//        //멤버 찾기 없으면 에러
//        Member member= memberRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOTFOUND));
//
//        //판매자 등록 여부
//        Boolean areYouSeller = sellerRepository.existsByMemberIdx(member);
//
//        //판매자로 등록되어 있으면
//        if(Boolean.TRUE.equals(areYouSeller)) {
//
//            //판매자 불러오기: 판매자 레포에서 멤버를 넣고 판매자를 찾는다.
//            Seller sellerFound = sellerRepository.findSellerByMemberIdx(member);
//
//            //판매자의 물품 리스트(엔티티)를 불러오기: 판매자 id 넣고 isDeleted = false인 것들만.
//            List<Product> productList = productRepository.findAllByIsDeletedEqualsAndSellerIdx(false, sellerFound.getId());
//
//
//            List<AllProductsResponse> allProductsResponseList = new ArrayList<>();
//
//
//
//        }  else throw new CustomException(ErrorCode.SELLER_ONLY);
//
//    }
















//    //요청 id 넣고 해당 유저 찾기 없으면 에러
//    Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
//
//
//    //판매자 생성
//    Seller newSeller = Seller.builder()
//            .memberIdx(member)
//            .isDeleted(false)
//            .build();
//
//        newSeller.getMemberIdx().setId(member.getId());
//
//        sellerRepository.save(newSeller);
//
//        return newSeller.getId();
//
//                }


    //private final ProductMapper productMapper;

    public Page<Product> findItemsPagination(String order, Pageable pageable){

        //최신순
        Page<Product> productEntities = productRepository.findAll(pageable);

        if(order.equals("priceAsc")){ //가격 오름차순
            productEntities = productRepository.findProductByOrderByPrice(pageable);
        }
        else if(order.equals("priceDesc")){ //가격 내림차순
            productEntities = productRepository.findProductByOrderByPriceDesc(pageable);
        }

        //인기순(판매량순)

        //return productEntities.map(productMapper.INSTANCE::ProductEntityToProductResponse);
        return productEntities;
    }

    public Page<Product> findItemsByCategoryIdxPagination(Integer categoryIdx, Pageable pageable) {
        Page<Product> productEntities = productRepository.findProductByProductsCategoryIdxId(categoryIdx, pageable);
        return productEntities;
    }

    public Map<String, Object> findProductsByOption(List<String> options, Pageable pageable) {
        //옵션디테일 레포지토리에서 options에 해당하는 상품을 각각 찾아서 교집합 찾기
        return new HashMap<>();
    }
}

