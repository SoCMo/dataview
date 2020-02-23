package com.nCov.DataView.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * program: AllAreaRequest
 * description: 全部地区信息请求
 * author: SoCMo
 * create: 2020/2/23
 */
@Data
public class AllAreaRequest {
    @NotBlank(message = "日期不能为空")
    private String date;

    @Range(min = 0, max = 5, message = "排列方式错误")
    @NotNull(message = "排序类型不能为空")
    private Integer order;

    @Pattern(regexp = "-?1", message = "排列顺序错误")
    @NotNull(message = "升降序类型不能为空")
    private String isUp;
}
