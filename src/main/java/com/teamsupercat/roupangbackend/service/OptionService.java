package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.dto.option.OptionDetailResponse;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.dto.option.request.OptionRegisterRequest;
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

    public Map<String, Object> findOptionByProductIdx(Integer productIdx) {
        Map<String, Object> options = new HashMap<>();

        options.put("productIdx", productIdx);
        //log.info("product idx = {}",productIdx);
        List<OptionType> optionTypes = optionTypeRepository.findOptionTypeByProductIdx(productIdx);
        //List<OptionType> optionTypes = optionTypeRepository.findAll();
        //log.info("option type list size={}", optionTypes.size());
        //List<String> optionDetailList;
        List<OptionTypeResponse> optionTypeResponseList = new ArrayList<>();
        List<OptionDetailResponse> optionDetailList;

        for(OptionType optionType : optionTypes){
            log.info("option type={}", optionType);
            //optionDetailList = new ArrayList<>();
            String optionTypeName = optionTypeNameRepository.findOptionTypeNameById(optionType.getOptionTypeNameIdx()).getOptionName();

            optionDetailList = new ArrayList<>();
            String optionDetailIdx = optionType.getOptionDetailIdx();

            String[] optionDetailIdxes = optionDetailIdx.split(",");

            for(int i=0; i<optionDetailIdxes.length; i++){
                log.info("idx={}", Integer.parseInt(optionDetailIdxes[i]));
                OptionDetail optionDetail = optionDetailRepository.findOptionDetailById(Integer.parseInt(optionDetailIdxes[i]));
                //optionDetailList.add(optionDetail.getOptionDetailName());
                optionDetailList.add(new OptionDetailResponse(optionDetail.getId(), optionDetail.getOptionDetailName()));
            }

            optionTypeResponseList.add(new OptionTypeResponse(optionType.getId(), optionType.getOptionTypeNameIdx(), optionTypeName, optionDetailList));
        }

        options.put("options", optionTypeResponseList);

        return options;
    }

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

    public Page<ProductResponse> findProductByOptionDetailName(List<String> options){
        //TODO. 코드 개선이 필요해보임.
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
                productList.retainAll(tempProductList);
            }
        }

        return new PageImpl<>(productList.stream().map(product -> new ProductResponse().toDto(product)).distinct().collect(Collectors.toList()));
    }

    //TODO. option_type 테이블에 자꾸 option_type_name 컬럼이 생기는데 아직 이유를 모르겠음.
    @Transactional
    public Map<String, Object> registerOptionOfProduct(OptionRegisterRequest optionRegisterRequest){

        int productIdx = optionRegisterRequest.getProductIdx();
        String optionTypeName = optionRegisterRequest.getOptionTypeName();
        List<String> optionDetailNames = optionRegisterRequest.getOptionDetailNames();
        Integer optionTypeNameIdx = 0;

        Product product = productRepository.findProductById(productIdx);

        //option type name
        Optional<OptionTypeName> optionTypeName1 = optionTypeNameRepository.findOptionTypeNameByOptionName(optionTypeName);

        if(optionTypeName1.isEmpty()){
            optionTypeNameIdx = optionTypeNameRepository.save(new OptionTypeName(optionTypeName)).getId();
        }
        else{
            optionTypeNameIdx = optionTypeName1.get().getId();
        }

        //option type
        OptionType optionType = optionTypeRepository.save(new OptionType(productIdx, optionTypeNameIdx, ""));

        //option detail
        StringBuilder optionDetailIdxSb = new StringBuilder();
        for(String optionDetailName : optionDetailNames){
            OptionDetail optionDetail = optionDetailRepository.save(new OptionDetail(product, optionDetailName, optionType.getId(), optionTypeNameIdx));
            optionDetailIdxSb.append(optionDetail.getId());
            optionDetailIdxSb.append(",");
        }

        optionDetailIdxSb.deleteCharAt(optionDetailIdxSb.length()-1);

        optionType.setOptionDetailIdx(optionDetailIdxSb.toString());
        optionTypeRepository.save(optionType);

        return findOptionByProductIdx(productIdx);
    }
}
