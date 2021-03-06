package com.nCov.DataView.model.request;

import com.nCov.DataView.model.response.info.SiteInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class RouteCalRequest extends SiteInfo {
    @Size(min = 2, message = "城市不得少于一个")
    private List<String> citys;

    @Range(min = 4, max = 4, message = "交通类型错误")
    private int type;

    @NotBlank(message = "名称不能为空")
    private String title;

    @NotBlank(message = "出发站不能为空")
    private String start;

    @NotBlank(message = "终点站不能为空")
    private String end;

    @NotNull(message = "距离不能为空")
    private double distance;

    @NotBlank
    private String startAdressZone;
}
