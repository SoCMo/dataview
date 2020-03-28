package com.nCov.DataView.controller;

import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.MapService;
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
     * @Description: 各个省每日疫情数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    @GetMapping("/provinceInfo")
    public Result provinceInfo() {
        return mapService.provinceInfo();
    }

    /**
     * @Description: 每日各个省数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    @GetMapping("/dateInfo")
    public Result dateInfo() {
        return mapService.dateInfo();
    }
}
