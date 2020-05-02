package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * program: startAndEndCalRequest
 * description: 已知起点终点计算风险的请求体
 * author: SoCMo
 * create: 2020/5/1
 */
@Data
public class StartAndEndCalRequest {
    @NotEmpty(message = "起点不能为空")
    private String start;

    @NotEmpty(message = "终点不能为空")
    private String end;
}
