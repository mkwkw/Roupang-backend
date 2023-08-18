package com.teamsupercat.roupangbackend.dto.option.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OptionRegisterRequest {

    private int productIdx;
    private String optionTypeName;
    private List<String> optionDetailNames;

}
