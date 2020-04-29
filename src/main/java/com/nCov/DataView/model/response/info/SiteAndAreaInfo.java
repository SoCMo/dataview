package com.nCov.DataView.model.response.info;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * program: SiteAndAreaInfo
 * description: 存储站点名称和站点所处区域
 * author: pongshy
 * create: 2020/4/29
 */
@Data
public class SiteAndAreaInfo {
    @NotBlank(message = "站点名不能为空")
    private String name;

    @NotBlank(message = "区域不能为空")
    private String area;
}
