package com.nCov.DataView.model.response.info;

import lombok.Data;

import java.util.List;

/**
 * program: SiteInfo
 * description: 存储站点列表，站点总数
 * author: pongshy
 * create: 2020/4/29
 */
@Data
public class SiteInfo {
    //存储每一段经过的站点，和站点所在的城市或区
    private List<SiteAndAreaInfo> siteNames;

    //站点的总数
    private Integer allSiteNumber;

}
