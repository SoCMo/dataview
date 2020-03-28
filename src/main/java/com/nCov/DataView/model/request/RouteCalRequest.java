package com.nCov.DataView.model.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * program: RouteCalRequest
 * description: 路途风险评估请求体
 * author: SoCMo
 * create: 2020/3/26
 */
@Data
public class RouteCalRequest {
    @Size(min = 1, message = "城市不得少于一个")
    List<String> citys;

    @Range(min = 0, max = 4, message = "交通类型错误")
    int type;

    @NotBlank(message = "名称不能为空")
    String title;

    @NotBlank(message = "出发站不能为空")
    String start;

    @NotBlank(message = "终点站不能为空")
    String end;

    @NotNull(message = "距离不能为空")
    double distance;
}
