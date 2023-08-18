package com.teamsupercat.roupangbackend.dto.viewhistory.request;

import com.teamsupercat.roupangbackend.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewHistoryRequest {

    private Integer productHistory;
}
