package com.nCov.DataView.controller;

import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.EpidemicService;
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
    @PostMapping("/allAreaInfo")
    public Result areaInfo(@Validated @RequestBody AllAreaRequest allAreaRequest) {
        return epidemicService.allAreaInfo(allAreaRequest);
    }
}
