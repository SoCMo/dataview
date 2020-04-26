package com.nCov.DataView.controller;

import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.MapService;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * program: CNMapController
 * description: 疫情地图
 * author: SoCMo
 * create: 2020/2/28
 */

@CrossOrigin
@RestController
@RequestMapping("/map")
public class MapController {
    @Resource
    private MapService mapService;

    /**
     * @Description: 每日各个省数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    @Cacheable(key = "dateInfo", value = "date")
    @GetMapping("/dateInfo")
    public Result dateInfo(@Param("date") String date) {
        return mapService.dateInfo(date);
    }

    /**
     * @Description: 统计数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/26
     */
    @GetMapping("/statistic")
    public Result statistic() {
        return mapService.statistic();
    }
}
