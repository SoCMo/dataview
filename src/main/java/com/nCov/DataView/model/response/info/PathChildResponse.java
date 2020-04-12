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
    private List<PathResponse> train;

    private List<PathResponse> flight;

    private List<PathResponse> transit;

    private int flag;

    public PathChildResponse() {
        this.setTrain(new ArrayList<>());
        this.setFlight(new ArrayList<>());
        this.setTransit(new ArrayList<>());
    }
}
