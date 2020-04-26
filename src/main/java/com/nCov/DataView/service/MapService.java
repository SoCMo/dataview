package com.nCov.DataView.service;

import com.nCov.DataView.model.response.Result;

/**
 * program: MapService
 * description: 地图信息
 * author: SoCMo
 * create: 2020/2/28
 */
public interface MapService {
    /**
     * @Description: 每日各个省疫情数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    public Result dateInfo(String date);

    /**
     * @Description: 每日获取数据量
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/26
     */
    public Result statistic();
}
