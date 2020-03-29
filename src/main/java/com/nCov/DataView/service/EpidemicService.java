package com.nCov.DataView.service;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.request.RouteCalRequest;
import com.nCov.DataView.model.request.RouteStoreInfo;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.SumCalResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * program: EpidemicService
 * description: 疫情地图
 * author: SoCMo
 * create: 2020/2/21
 */
public interface EpidemicService {
    /**
     * @Description: 获取地区信息
     * @Param: [areaName]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    public Result areaInfo(AreaInfoRequest areaInfoRequest);

    /**
     * @Description: 获取全部地区信息
     * @Param: [areaName]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    public Result allAreaInfo(AllAreaRequest allAreaRequest);

    /**
     * @Description: 获取某地区全部疫情数据
     * @Param: [name]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/24
     */
    public Result allDateInfo(String name);

    /**
     * @Description: 各地疫情严重程度分析
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/16
     */
    public Result impDateInfo(String date);

    /**
     * @Description: 使用excel表格导入学生信息
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/28
     */
    public Result excelIn(MultipartFile file) throws AllException, IOException;

    /**
     * @Description: 路程评估
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/28
     */
    public Result routeCal(List<RouteCalRequest> routeCalRequestList);

    /**
     * @Description: 路径存储兼查询
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
    public Result routeStore(RouteStoreInfo routeStoreInfo);

    /**
     * @Description: 对数据库中所有数据进行回校评估，并返回
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/29
     */
    public Result getAllRouteCal();

    public SumCalResponse getRouteCal(List<RouteCalRequest> routeCalRequestList);
}
