package com.teamsupercat.roupangbackend.dto.option.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptionTypeRequest {


    @ApiModelProperty(name= "optionTypeNameIdx", value = "옵션 타입 ID", example = "1 이면 사이즈(S,M,L), 3이면 컬러(Red, Blue)")
    private Integer optionTypeNameIdx;

    @ApiModelProperty(name= "optionTypeName", value = "옵션 타입 이름", example = "상의")
    private String optionTypeName;

    private List<OptionDetailRequest> optionDetails;


}
