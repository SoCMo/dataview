package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: SumCalResponse
 * description: 同一起始地点和终点，计算各个线路的评分，以及错误地址集合
 * author: pongshy
 * create: 2020/3/31
 */
@Data
public class SumAssessmentResponse {
    List<AssessmentAllResponse> assessmentList;

    List<String> errorAddress;
}
