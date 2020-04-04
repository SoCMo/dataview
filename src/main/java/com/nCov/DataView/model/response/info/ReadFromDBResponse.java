package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: ReadFromDBResponse
 * description: 从数据库中读取数据，并计算风险返回
 * author: pongshy
 * create: 2020/4/4
 */
@Data
public class ReadFromDBResponse {
    private List<AssessmentAllResponse> assessmentAllResponseList;

    private int total;
}
