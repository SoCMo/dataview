package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: SumCalResponse
 * description: 评估总和
 * author: SoCMo
 * create: 2020/3/28
 */
@Data
public class SumCalResponse {
    List<RouteCalReponse> resultList;

    String sumScore;

    String Type;
}
