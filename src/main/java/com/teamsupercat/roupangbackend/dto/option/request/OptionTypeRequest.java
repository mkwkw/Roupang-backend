package com.teamsupercat.roupangbackend.dto.option.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.entity.OptionType;
import com.teamsupercat.roupangbackend.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;
@ToString
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptionTypeRequest {

    @ApiModelProperty(name= "optionTypeNameIdx", value = "OptionType의 옵션타입명 Id", example = "1 (1: 크기, 2: 무게, 3: 색상)")
    private Integer optionTypeNameIdx;
    private List<OptionDetailRequest> optionDetails;


    @Builder
    public OptionType createOptionType(Product savedProduct) {
        return OptionType.builder()
                .productIdx(savedProduct.getId())
                .optionTypeNameIdx(this.getOptionTypeNameIdx())
                .build();

    }
}
