package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: DayInfoResponse
 * description: 每日各个省疫情数据
 * author: SoCMo
 * create: 2020/2/28
 */
@Data
public class DayInfoResponse {
    private String date;

    private List<ProvinceInfo> provinceInfoList;
}
