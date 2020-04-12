package com.nCov.DataView.service;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.entity.PathInfoDO;
import com.nCov.DataView.model.request.*;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.AssessmentAllResponse;
import com.nCov.DataView.model.response.info.SumCalResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
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
     * @Description: 同一终点和起点的不同路径进行计算
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/29
     */
    public List<SumCalResponse> getAllRouteCal(List<PathInfoDO> pathInfoDOList) throws AllException, ParseException;

    /**
     * @Description: 数据库中存在一条线路进行分数计算
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/29
     */

    public SumCalResponse getRouteCal(List<RouteCalRequest> routeCalRequestList);

    /**
     * @Description: 前端发送地址信息，后端进行风险评估并返回分数
     * @Param: [AddressRequest]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    public Result getAssessment(AddressRequest data) throws AllException, IOException, ParseException;

    /**
     * @Description: 使用百度api查询该地址回校路径，并插入数据库中
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    public AssessmentAllResponse getScoreAndInsert(String startAddress, String start, String endAddress) throws AllException, IOException, ParseException;

    /**
     * @Description: 传入路径信息，计算分数
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    public SumCalResponse calculate(List<RouteCalRequest> routeCalRequestList) throws AllException, ParseException;

    /**
     * @Description: 读取数据库中存储的信息
     * @Param: [cur, nums]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    public Result getSpecifiedNumber(Integer cur, Integer nums, String provinceName) throws AllException, ParseException;

    /**
     * @Description: 读取excel表格中的信息，并存储到数据库中
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    public Result setInDataBase(MultipartFile file) throws AllException, IOException, ParseException;

    /**
     * @Description: 单个城市风险查询
     * @Param: [city]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/8
     */
    public Result cityQuery(CityRiskRequest cityRiskRequest);

    /**
     * @Description: 路径信息评估
     * @Param: [index, max, province]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/10
     */
    public Result pathQuery(PathQueryRequest pathQueryRequest);

    /**
     * @Description: 查询单条返校信息
     * @Param: [startAddress]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/12
     */
    public Result pathChildQuery(String startAddress);

    /**
     * @Description: 热力图绘制
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/12
     */
    public Result pathMap();

    /**
     * @Description: 将百度地图api的查询结果存入pathInfo,和passInfo中
     * @Param: [startaddress]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/12
     */
    public Result writeInPathAndPass() throws AllException, IOException, ParseException;
}
