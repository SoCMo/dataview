package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: CNMapResponse
 * description: 中国地图返回信息
 * author: SoCMo
 * create: 2020/2/28
 */
@Data
public class ProvinceInfoResponse {
    private String name;

    private List<DayInfo> dayInfoList;
}
