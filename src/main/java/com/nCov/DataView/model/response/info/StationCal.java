package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: StationCal
 * description: 站点信息
 * author: SoCMo
 * create: 2020/4/30
 */
@Data
public class StationCal {
    private String name;

    private String area;

    private Double risk;
}
