package com.teamsupercat.roupangbackend.controller;

import com.teamsupercat.roupangbackend.common.ResponseDto;
import com.teamsupercat.roupangbackend.dto.viewhistory.request.ViewHistoryRequest;
import com.teamsupercat.roupangbackend.dto.viewhistory.response.ViewHistoryResponse;
import com.teamsupercat.roupangbackend.service.ViewHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "최근 본 상품조회 API")
@Slf4j
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ViewHistoryController {
    private final ViewHistoryService viewHistoryService;

    @ApiOperation(value = "최근 본 상품목록 조회")
    @PostMapping("/view_history")
    public ResponseDto<?> viewHistory(@RequestBody List<ViewHistoryRequest> viewHistoryRequest) {
        List<ViewHistoryResponse> historyResponses = viewHistoryService.viewHistory(viewHistoryRequest);
        return ResponseDto.success(historyResponses);
    }
}
