package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: provinceInfo
 * description: 省疫情信息
 * author: SoCMo
 * create: 2020/2/28
 */
@Data
public class DayInfo {
    private String date;

    private Integer confirm;
}
