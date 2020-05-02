package com.nCov.DataView.model.response.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * program: CityCal
 * description: 城市疫情评估
 * author: SoCMo
 * create: 2020/3/28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityCal {
    private String cityname;

    private Double cityscore;
}
