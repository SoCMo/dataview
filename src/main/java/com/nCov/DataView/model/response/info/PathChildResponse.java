package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * program: pathChildResponse
 * description: 单地址返校风险
 * author: SoCMo
 * create: 2020/4/12
 */
@Data
public class PathChildResponse {
    private List<PathResponse> transit;

    public PathChildResponse() {
        this.setTransit(new ArrayList<>());
    }
}
