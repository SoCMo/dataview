package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.AreaDOMapper;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.entity.AreaDO;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.AreaInfoResponse;
import com.nCov.DataView.model.response.info.DateInfoResponse;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.ResultTool;
import com.nCov.DataView.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * program: EpidemicImpl
 * description: 疫情地图
 * author: SoCMo
 * create: 2020/2/21
 */
@Slf4j
@Service
public class EpidemicServiceImpl implements EpidemicService {
    @Resource
    private AreaDOMapper areaDOMapper;

    @Resource
    private CovDataMapper covDataMapper;

    /**
     * @Description: 获取地区信息
     * @Param: [areaName]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    @Override
    public Result areaInfo(AreaInfoRequest areaInfoRequest) {
        CovDataExample covDataExample = new CovDataExample();
        covDataExample.createCriteria()
                .andProvincenameLike(areaInfoRequest.getProvinceName() + "%")
                .andCitynameLike(areaInfoRequest.getCityName() + "%")
                .andDateEqualTo(TimeTool.stringToDay(areaInfoRequest.getDate()));
        try {
            List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
            if (covDataList.size() != 1) {
                throw new AllException(EmAllException.DATABASE_ERROR, "查询数据有误");
            } else {
                AreaInfoResponse areaInfoResponse = new AreaInfoResponse();
                BeanUtils.copyProperties(covDataList.get(0), areaInfoResponse);
                List<AreaDO> areaDOList = areaDOMapper.mapSelectByName(areaInfoRequest.getCityName() + "%");
                if (areaDOList.size() > 1) {
                    throw new AllException(EmAllException.DATABASE_ERROR, "查询数据有误");
                }
                areaInfoResponse.Calculation(areaDOList.size() == 1 ? areaDOList.get(0).getPopulation() : 0);
                return ResultTool.success(areaInfoResponse);
            }
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 获取全部地区信息
     * @Param: [areaName]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    @Override
    public Result allAreaInfo(AllAreaRequest allAreaRequest) {
        try {
            Map<String, AreaDO> cityMap = areaDOMapper.getCityMap();
            Map<Integer, AreaDO> provinceMap = areaDOMapper.getProvinceMap();
            if (cityMap.isEmpty() || provinceMap.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "地区信息未录入.");
            }

            Map<String, CovData> covDataMap = covDataMapper.getInfoByDate(allAreaRequest.getDate());
            if (covDataMap.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "日期数据不存在或传入参数错误");
            }

            List<AreaInfoResponse> areaInfoResponseList = new ArrayList<>();
            cityMap.forEach((key, value) -> {
                AreaInfoResponse areaInfoResponse = new AreaInfoResponse();
                if (covDataMap.get(key) == null) {
                    //模糊搜索
                    CovData covData = null;
                    for (CovData travel : covDataMap.values()) {
                        if (travel.getCityname().contains(key))
                            covData = travel;
                    }
                    if (covData == null) return;
                    else BeanUtils.copyProperties(covData, areaInfoResponse);
                } else {
                    BeanUtils.copyProperties(covDataMap.get(key), areaInfoResponse);
                }
                areaInfoResponse.Calculation(value.getPopulation());
                areaInfoResponseList.add(areaInfoResponse);
            });
            AreaInfoResponse.init(Integer.valueOf(allAreaRequest.getIsUp()), allAreaRequest.getOrder());
            areaInfoResponseList.sort(AreaInfoResponse::compareTo);
            return ResultTool.success(areaInfoResponseList);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 获取某地区全部疫情数据
     * @Param: [name]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/24
     */
    @Override
    public Result allDateInfo(String name) {
        try {
            CovDataExample covDataExample = new CovDataExample();
            covDataExample.createCriteria().andCitynameLike(name + "%");
            covDataExample.setOrderByClause("date ASC");
            List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
            if (covDataList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "搜索地区暂无疫情数据");
            }

            AreaDO areaDO = areaDOMapper.nameLike(name);
            if (areaDO == null) {
                throw new AllException(EmAllException.DATABASE_ERROR, "暂无地区数据");
            }

            List<DateInfoResponse> dateInfoResponseList = new ArrayList<>();
            covDataList.forEach(covData -> {
                DateInfoResponse dateInfoResponse = new DateInfoResponse();
                BeanUtils.copyProperties(covData, dateInfoResponse);
                dateInfoResponse.Calculation(areaDO.getPopulation());
                dateInfoResponse.setDate(TimeTool.timeToDaySy(covData.getDate()));
                dateInfoResponseList.add(dateInfoResponse);
                System.out.println(name + ": " + covData.getDate());
            });
            return ResultTool.success(dateInfoResponseList);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }
}
