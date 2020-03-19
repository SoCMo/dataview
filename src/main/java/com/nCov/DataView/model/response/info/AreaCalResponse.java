package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: AreaCalResponse
 * description: 地区信息返回
 * author: SoCMo
 * create: 2020/3/19
 */
@Data
public class AreaCalResponse {
    double number;

    List<AreaCalInfo> areaList;
}

