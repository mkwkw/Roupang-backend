package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.dto.option.OptionDetailResponse;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
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
}
