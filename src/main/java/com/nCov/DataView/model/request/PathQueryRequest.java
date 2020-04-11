package com.nCov.DataView.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * program: PathQueryRequest
 * description: 路径信息请求体
 * author: SoCMo
 * create: 2020/4/10
 */
@Data
public class PathQueryRequest {
    @Min(value = 1, message = "偏移值最小为1")
    int index;

    @NotNull(message = "获取数不能为空")
    int num;

    @NotBlank(message = "省市不能为空")
    String province;
}
