package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.dto.option.OptionDetailResponse;
import com.teamsupercat.roupangbackend.dto.option.OptionTypeResponse;
import com.teamsupercat.roupangbackend.entity.OptionDetail;
import com.teamsupercat.roupangbackend.entity.OptionType;
import com.teamsupercat.roupangbackend.repository.OptionDetailRepository;
import com.teamsupercat.roupangbackend.repository.OptionTypeNameRepository;
import com.teamsupercat.roupangbackend.repository.OptionTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OptionService {

    private final OptionTypeRepository optionTypeRepository;
    private final OptionDetailRepository optionDetailRepository;
    private final OptionTypeNameRepository optionTypeNameRepository;

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
}
