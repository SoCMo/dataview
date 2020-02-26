package com.nCov.DataView.controller;

import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.EpidemicService;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * program: AreaInfoController
 * description: 地区疫情情况
 * author: SoCMo
 * create: 2020/2/21
 */
@CrossOrigin
@RestController
@RequestMapping("/epidemic")
public class EpidemicController {
    @Resource
    private EpidemicService epidemicService;

    /**
     * @Description: 获取地区疫情数据
     * @Param: [areaInfoRequest]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    @Cacheable(value = "areaInfo", key = "#allAreaRequest")
    @PostMapping("/areaInfo")
    public Result areaInfo(@Validated @RequestBody AreaInfoRequest areaInfoRequest) {
        return epidemicService.areaInfo(areaInfoRequest);
    }

    /**
     * @Description: 所有地区信息
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/22
     */
    @Cacheable(value = "allAreaInfo", key = "#allAreaRequest")
    @PostMapping("/allAreaInfo")
    public Result allAreaInfo(@Validated @RequestBody AllAreaRequest allAreaRequest) {
        return epidemicService.allAreaInfo(allAreaRequest);
    }

    /**
     * @Description: 获取某地区全部疫情数据
     * @Param: [name]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/24
     */
    @Cacheable(value = "dateInfo", key = "#name")
    @GetMapping("/allDateInfo")
    public Result dateInfo(@Param("name") String name) {
        return epidemicService.allDateInfo(name);
    }
}
