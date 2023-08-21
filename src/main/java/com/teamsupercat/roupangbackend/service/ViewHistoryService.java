package com.teamsupercat.roupangbackend.service;

import com.teamsupercat.roupangbackend.common.CustomException;
import com.teamsupercat.roupangbackend.common.ErrorCode;
import com.teamsupercat.roupangbackend.dto.viewhistory.request.ViewHistoryRequest;
import com.teamsupercat.roupangbackend.dto.viewhistory.response.ViewHistoryResponse;
import com.teamsupercat.roupangbackend.entity.Product;
import com.teamsupercat.roupangbackend.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ViewHistoryService {


    private final ProductRepository productRepository;

    public List<ViewHistoryResponse> viewHistory(List<ViewHistoryRequest> viewHistoryRequest) {
        List<ViewHistoryResponse> historyResponses = new ArrayList<>();
        ViewHistoryResponse viewHistoryResponse = new ViewHistoryResponse();

        for (ViewHistoryRequest request : viewHistoryRequest) {

            Product product = productRepository.findById(request.getProductHistory()).orElseThrow(() -> new CustomException(ErrorCode.SHOP_PRODUCT_NOT_FOUND));
            ViewHistoryResponse historyResponse = viewHistoryResponse.toEntity(product);
            historyResponses.add(historyResponse);
        }

        return historyResponses;
    }
}
