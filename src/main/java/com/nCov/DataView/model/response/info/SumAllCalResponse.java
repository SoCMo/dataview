package com.nCov.DataView.model.response.info;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * program: SumAllCalResponse
 * description: 评估总和和开始地址和结束地址
 * author: pongshy
 * create: 2020/3/29
 */
@Data
public class SumAllCalResponse {
    List<RouteCalReponse> resultList;

    String sumScore;

    String startAddress;

    String endAddress;
}
