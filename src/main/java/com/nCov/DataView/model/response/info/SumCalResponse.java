package com.nCov.DataView.model.response.info;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * program: SumCalResponse
 * description: 评估总和
 * author: SoCMo
 * create: 2020/3/28
 */
@Data
@NoArgsConstructor
public class SumCalResponse {
    private List<RouteCalReponse> resultList;

    private String sumScore;

    private String sumTime;

    private String type;

    private Double price;
}
