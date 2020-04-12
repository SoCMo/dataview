package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: PathQueryResponse
 * description: 返校路径返回体
 * author: SoCMo
 * create: 2020/4/12
 */
@Data
public class PathQueryResponse {
    private String start;

    private String end;

    private double risk;
}
