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

    @ApiModelProperty(name= "optionTypeNameIdx", value = "OptionType의 옵션타입명 Id", example = "1 (1: 크기, 2: 무게, 3: 색상)")
    private Integer optionTypeNameIdx;
    private List<OptionDetailRequest> optionDetails;


}
