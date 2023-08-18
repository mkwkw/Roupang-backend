package com.teamsupercat.roupangbackend.dto.option.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionTypeRequest {

    private Integer optionTypeIdx;
    private String optionTypeName;
    private List<OptionDetailRequest> optionDetails;

}
