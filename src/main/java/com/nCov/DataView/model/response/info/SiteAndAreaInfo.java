package com.nCov.DataView.model.response.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * program: SiteAndAreaInfo
 * description: 存储站点名称和站点所处区域
 * author: pongshy
 * create: 2020/4/29
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class  SiteAndAreaInfo {
    @NotBlank(message = "站点名不能为空")
    private String name;

    @NotBlank(message = "区域不能为空")
    private String area;
}
