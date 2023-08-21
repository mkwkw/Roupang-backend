package com.teamsupercat.roupangbackend.dto.option.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.teamsupercat.roupangbackend.entity.OptionDetail;
import com.teamsupercat.roupangbackend.entity.OptionType;
import com.teamsupercat.roupangbackend.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
@ToString
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OptionDetailRequest {

    @ApiModelProperty(name = "optionDetailName", value = "옵션 세부 선택", example = "Red (S/M/L, Red/Blue)")
    private String optionDetailName;


    public OptionDetail createOptionDetail(Product savedProduct, OptionType optionType) {
        return OptionDetail.builder()
                .productIdx(savedProduct)
                .optionDetailName(this.getOptionDetailName())
                .optionTypeIdx(optionType.getId())
                .optionTypeNameIdx(optionType.getOptionTypeNameIdx())
                .build();

    }
}

