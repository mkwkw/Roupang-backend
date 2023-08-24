package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.option.OptionDetailResponse;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.dto.option.request.OptionRegisterRequest;
import com.teamsupercat.roupangbackend.dto.option.request.OptionWithProductRegisterRequest;
import com.teamsupercat.roupangbackend.dto.product.ProductResponse;
import com.teamsupercat.roupangbackend.entity.OptionDetail;
import com.teamsupercat.roupangbackend.entity.OptionType;
import com.teamsupercat.roupangbackend.entity.OptionTypeName;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.repository.OptionDetailRepository;
import com.teamsupercat.roupangbackend.repository.OptionTypeNameRepository;
import com.teamsupercat.roupangbackend.repository.OptionTypeRepository;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionTypeRepository optionTypeRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final OptionTypeNameRepository optionTypeNameRepository;
    private final ProductRepository productRepository;

    //해당 물품의 모든 옵션 조회
    public Map<String, Object> findOptionByProductIdx(Integer productIdx) {
        Map<String, Object> options = new HashMap<>();

        options.put("productIdx", productIdx);

        List<OptionType> optionTypes = optionTypeRepository.findOptionTypeByProductIdx(productIdx);
        List<OptionTypeResponse> optionTypeResponseList = new ArrayList<>();
        List<OptionDetailResponse> optionDetailList;

        for(OptionType optionType : optionTypes){
            String optionTypeName = optionTypeNameRepository.findOptionTypeNameById(optionType.getOptionTypeNameIdx()).getOptionName();

            optionDetailList = new ArrayList<>();
            String optionDetailIdx = optionType.getOptionDetailIdx();

            String[] optionDetailIdxes = optionDetailIdx.split(",");

            for(int i=0; i<optionDetailIdxes.length; i++){
                OptionDetail optionDetail = optionDetailRepository.findOptionDetailById(Integer.parseInt(optionDetailIdxes[i]));
                optionDetailList.add(new OptionDetailResponse(optionDetail.getId(), optionDetail.getOptionDetailName()));
            }

            optionTypeResponseList.add(new OptionTypeResponse(optionType.getId(), optionType.getOptionTypeNameIdx(), optionTypeName, optionDetailList));
        }

        options.put("options", optionTypeResponseList);

        return options;
    }

    //DB에 있는 모든 옵션 조회
    public Map<String, Object> findAllOption() {

        Map<String, Object> answer = new HashMap<>();
        List<OptionTypeName> optionTypeNameList = optionTypeNameRepository.findAll();

        for(OptionTypeName optionTypeName : optionTypeNameList){
            List<OptionDetail> optionDetailList = optionDetailRepository.findOptionDetailByOptionTypeNameIdx(optionTypeName.getId());
            List<String> optionNameList = new ArrayList<>();

            if(optionDetailList.size()==0){
                optionNameList.add("선택 옵션이 없습니다.");
            }
            for(OptionDetail optionDetail : optionDetailList){
                optionNameList.add(optionDetail.getOptionDetailName());
            }

            answer.put(optionTypeName.getOptionName(), optionNameList.stream().distinct().collect(Collectors.toList()));
        }

        return answer;
    }

    //옵션으로 물품 검색
    public Page<ProductResponse> findProductByOptionDetailName(List<String> options){
        //stream으로 개선!
        List<Product> productList = new ArrayList<>();

        for(String optionDetailName : options){
            List<OptionDetail> optionDetailList = optionDetailRepository.findOptionDetailByOptionDetailName(optionDetailName);
            List<Product> tempProductList = new ArrayList<>();
            for(OptionDetail optionDetail : optionDetailList){
                tempProductList.add(optionDetail.getProductIdx());
            }

            if(productList.isEmpty()){
                productList = tempProductList;
            }
            else{
                productList.retainAll(tempProductList); //옵션으로 검색한 결과의 교집합
            }
        }

        if(productList.isEmpty()){
            throw new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND);
        }

        //List -> Page
        return new PageImpl<>(productList.stream().map(product -> new ProductResponse().toDto(product)).distinct().collect(Collectors.toList()));
    }

    //옵션 등록(단독 API)
    @Transactional
    public Map<String, Object> registerOptionOfProduct(OptionRegisterRequest optionRegisterRequest){
        //stream으로 가독성 개선!
        int productIdx = optionRegisterRequest.getProductIdx();
        String optionTypeName = optionRegisterRequest.getOptionTypeName();
        List<String> optionDetailNames = optionRegisterRequest.getOptionDetailNames();

        Product product = productRepository.findProductById(productIdx);

        //option type name
//        Optional<OptionTypeName> optionTypeName1 = optionTypeNameRepository.findOptionTypeNameByOptionName(optionTypeName);
//        if(optionTypeName1.isEmpty()){
//            optionTypeNameIdx = optionTypeNameRepository.save(new OptionTypeName(optionTypeName)).getId();
//        }
//        else{
//            optionTypeNameIdx = optionTypeName1.get().getId();
//        }

        //위에 코드를 stream으로 간단하게 변경
        Integer optionTypeNameIdx = optionTypeNameRepository.findOptionTypeNameByOptionName(optionTypeName).orElse(optionTypeNameRepository.save(new OptionTypeName(optionTypeName))).getId();

        //option type
        OptionType optionType = optionTypeRepository.save(new OptionType(productIdx, optionTypeNameIdx, ""));

        //option detail
        StringBuilder optionDetailIdxSb = new StringBuilder();

        //아래 코드를 stream으로 간단하게 변경
        optionDetailNames.forEach(optionDetailName -> optionDetailIdxSb.append(optionDetailRepository.save(new OptionDetail(product, optionDetailName, optionType.getId(), optionTypeNameIdx)).getId()).append(","));
//        for(String optionDetailName : optionDetailNames){
//            OptionDetail optionDetail = optionDetailRepository.save(new OptionDetail(product, optionDetailName, optionType.getId(), optionTypeNameIdx));
//            optionDetailIdxSb.append(optionDetail.getId());
//            optionDetailIdxSb.append(",");
//        }

        optionDetailIdxSb.deleteCharAt(optionDetailIdxSb.length()-1);

        optionType.setOptionDetailIdx(optionDetailIdxSb.toString());
        optionTypeRepository.save(optionType);

        return findOptionByProductIdx(productIdx);
    }

    //옵션 등록(물품 등록 API에 포함)
    @Transactional
    public void registerOptionOfProduct(OptionWithProductRegisterRequest optionWithProductRegisterRequest, Product savedProduct){
        //stream으로 가독성 개선!
//      int productIdx = optionWithProductRegisterRequest.getProductIdx();
        String optionTypeName = optionWithProductRegisterRequest.getOptionTypeName();
        List<String> optionDetailNames = optionWithProductRegisterRequest.getOptionDetailNames();
        //Integer optionTypeNameIdx = 0;

        Product product = productRepository.findProductById(savedProduct.getId());

        //option type name
//        Optional<OptionTypeName> optionTypeName1 = optionTypeNameRepository.findOptionTypeNameByOptionName(optionTypeName);
//
//        if(optionTypeName1.isEmpty()){
//            optionTypeNameIdx = optionTypeNameRepository.save(new OptionTypeName(optionTypeName)).getId();
//        }
//        else{
//            optionTypeNameIdx = optionTypeName1.get().getId();
//        }
//
        //option type name 중복 허용
        Integer optionTypeNameIdx = optionTypeNameRepository.save(new OptionTypeName(optionTypeName)).getId();
        //option type
        OptionType optionType = optionTypeRepository.save(new OptionType(savedProduct.getId(), optionTypeNameIdx, ""));

        //아래 코드를 stream으로 간단하게 변경
        StringBuilder optionDetailIdxSb = new StringBuilder();
//        for(String optionDetailName : optionDetailNames){
//            OptionDetail optionDetail = optionDetailRepository.save(new OptionDetail(product, optionDetailName, optionType.getId(), optionTypeNameIdx));
//            optionDetailIdxSb.append(optionDetail.getId());
//            optionDetailIdxSb.append(",");
//        }
//
        //option detail
        optionDetailNames.forEach(optionDetailName -> optionDetailIdxSb.append(optionDetailRepository.save(new OptionDetail(product, optionDetailName, optionType.getId(), optionTypeNameIdx)).getId()).append(","));


        optionDetailIdxSb.deleteCharAt(optionDetailIdxSb.length()-1);

        optionType.setOptionDetailIdx(optionDetailIdxSb.toString());
        optionTypeRepository.save(optionType);

//        return findOptionByProductIdx(savedProduct.getId();
    }
}
