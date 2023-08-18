package com.teamsupercat.roupangbackend.dto.option.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OptionDetailRequest {
    private Integer optionDetailIdx;
    private String optionDetailName;
}
