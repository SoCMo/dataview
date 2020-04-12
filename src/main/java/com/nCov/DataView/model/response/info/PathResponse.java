package com.nCov.DataView.model.response.info;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;

/**
 * program: PathCalResponse
 * description: 单挑返校路径信息
 * author: SoCMo
 * create: 2020/4/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PathResponse extends SumCalResponse {
    private String time;

    public PathResponse() {
        this.setResultList(new ArrayList<>());
    }
}
