package com.nCov.DataView.controller;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.request.*;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.ResultTool;
import org.apache.ibatis.annotations.Param;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;

/**
 * program: AreaInfoController
 * description: 地区疫情情况
 * author: SoCMo
 * create: 2020/2/21
 */
@CrossOrigin
@EnableAsync
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
     * @Description: 使用excel表格导入学生信息
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/28
     */
    @PostMapping("/informationByExcel")
    public Result InformationByExcel(@RequestBody MultipartFile file) throws AllException, IOException {
        epidemicService.excelIn(file);
        return ResultTool.success("导入成功");
    }

    /**
     * @Description: 路径存储与评估
     * @Param: [routeStoreInfo]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
    @PostMapping("/routeStore")
    public Result routeStore(@Validated @RequestBody PathRequest pathRequest) {
        return epidemicService.routeStore(pathRequest);
    }

    /**
     * @Description: 前端发送地址信息，后端进行风险评估并返回分数
     * @Param: [AddressRequest]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    @PostMapping("/assessment")
    public Result assessCal(@Validated @RequestBody AddressRequest data) throws IOException, AllException, ParseException {
        return epidemicService.getAssessment(data);
    }

    /**
     * @Description: 读取数据库中存储的信息
     * @Param: [cur, nums]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    @GetMapping("/getDetails")
    public Result getDetails(@RequestParam(value = "cur", defaultValue = "-1") Integer cur,
                             @RequestParam(value = "nums", defaultValue = "-1") Integer nums,
                             @RequestParam(value = "province", defaultValue = "1") String province) throws AllException, ParseException {
        if (cur <= 0 || nums <= 0 || province.equals("1")) {
            return ResultTool.error(500, "传参有误 ");
        }

        StringBuilder provinceName = new StringBuilder();
        if (province.length() <= 4) {
            provinceName.append("中国");
            String temp = "";
            if (province.contains("省")) {
                temp = province.replace("省", "");
            }
            else if (province.contains("市")) {
                temp = province.replace("市", "");
            }
            else {
                temp = province;
            }
            provinceName.append(temp);
        } else {
            provinceName.append(province);
        }
        System.out.println(1);
        System.out.println(provinceName.toString());

        return epidemicService.getSpecifiedNumber(cur, nums, provinceName.toString());
    }

    /**
     * @Description: 读取excel表格中的信息，并存储到数据库中
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    @PostMapping("/storeByExcel")
    public Result storeByExcel(@RequestBody MultipartFile file) throws AllException, IOException, ParseException {
        epidemicService.setInDataBase(file);
        String msg = "上传成功，正在读取请稍后";

        return ResultTool.success(msg);
    }

}
