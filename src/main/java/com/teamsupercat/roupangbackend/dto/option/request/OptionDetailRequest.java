package com.teamsupercat.roupangbackend.dto.option.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptionDetailRequest {

    @ApiModelProperty(name= "optionDetailName", value = "옵션 세부 선택", example = "Red (S/M/L, Red/Blue)")
    private String optionDetailName;

}

