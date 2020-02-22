package com.nCov.DataView.model.request;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * program: areaInfoRequest
 * description: 地区查询接口
 * author: SoCMo
 * create: 2020/2/21
 */
@Data
@Validated
public class AreaInfoRequest {
    @NotBlank(message = "省名不能为空")
    String provinceName;

    @NotBlank(message = "城市名不能为空")
    String cityName;

    @NotBlank(message = "时间不能为空")
    String date;
}
