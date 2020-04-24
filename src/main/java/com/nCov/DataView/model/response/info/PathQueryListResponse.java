package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * program: PathQueryListResponse
 * description: 返校风险列表返回
 * author: SoCMo
 * create: 2020/4/13
 */
@Data
public class PathQueryListResponse {
    private List<PathQueryResponse> pathQueryResponseList;

    private int num;

    private double average;

    public PathQueryListResponse() {
        this.pathQueryResponseList = new ArrayList<>();
    }
}
