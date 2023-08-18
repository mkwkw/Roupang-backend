package com.teamsupercat.roupangbackend.dto.option;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionTypeResponse {

    private Integer optionTypeIdx;
    private Integer optionTypeNameIdx;
    private String optionTypeName;
    private List<OptionDetailResponse> optionDetails;
}
