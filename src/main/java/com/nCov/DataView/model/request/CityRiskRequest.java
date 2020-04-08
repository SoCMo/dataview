package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * program: CityRiskRequest
 * description: 单城市风险评估数据
 * author: SoCMo
 * create: 2020/4/8
 */
@Data
public class CityRiskRequest {
    private String province;

    @NotBlank(message = "市名不能为空")
    private String name;

    private String date;
}
