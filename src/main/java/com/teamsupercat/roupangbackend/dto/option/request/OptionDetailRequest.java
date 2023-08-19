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

    @ApiModelProperty(name= "optionTypeName", value = "옵션 타입 이름", example = "상의")
    private Integer optionDetailIdx;

    @ApiModelProperty(name= "optionDetailName", value = "옵션 디테일 이름", example = "사이즈면 S or M or L 하나 입력")
    private String optionDetailName;
}
