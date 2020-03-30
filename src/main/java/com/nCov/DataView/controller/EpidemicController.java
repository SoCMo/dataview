package com.nCov.DataView.controller;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.request.RouteListRequest;
import com.nCov.DataView.model.request.RouteStoreInfo;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.EpidemicService;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

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

    /**
     * @Description: 各地风险评估
     * @Param: [date]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/18
     */
    @Cacheable(value = "impDateInfo", key = "#date")
    @GetMapping("/impDateInfo")
    public Result impDateInfo(@Param("date") String date) {
        return epidemicService.impDateInfo(date);
    }

    /**
     * @Description: 回校评估
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/28
     */
    @PostMapping("/routeCal")
    public Result routeCal(@Validated @RequestBody RouteListRequest RouteListRequest) {
        return epidemicService.routeCal(RouteListRequest.getRouteCalRequestList());
    }

    /**
     * @Description: 对数据库中所有数据进行回校评估，并返回
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/29
     */
    @GetMapping("/allRouteCal")
    public Result allRouteCal() {

        return epidemicService.getAllRouteCal();
    }



    /**
     * @Description: 使用excel表格导入学生信息
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/28
     */
    @PostMapping("/informationByExcel")
    public Result InformationByExcel(@RequestBody MultipartFile file) throws AllException, IOException {
        return epidemicService.excelIn(file);
    }

    /**
     * @Description: 路径存储与评估
     * @Param: [routeStoreInfo]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
    @PostMapping("/routeStore")
    public Result routeStore(@Validated @RequestBody RouteStoreInfo routeStoreInfo) {
        return epidemicService.routeStore(routeStoreInfo);
    }
}
