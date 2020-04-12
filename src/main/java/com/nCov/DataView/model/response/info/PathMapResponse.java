package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: PathMapResponse
 * description: 热力图绘制点
 * author: SoCMo
 * create: 2020/4/12
 */
@Data
public class PathMapResponse {
    private double lng;

    private double lat;

    private double count;
}
