package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: CityCal
 * description: 城市疫情评估
 * author: SoCMo
 * create: 2020/3/28
 */
@Data
public class CityCal {
    private String cityname;

    private Integer cityscore;
}
