package com.nCov.DataView.model.response.info;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * program: StatisticResponse
 * description: 每日获取数据
 * author: SoCMo
 * create: 2020/4/26
 */
@Data
@NoArgsConstructor
public class StatisticResponse {
    private Integer covData;

    private Integer abroadInput;

    private Integer sumNumber;
}
