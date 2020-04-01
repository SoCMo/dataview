package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.*;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.ConstCorrespond;
import com.nCov.DataView.model.entity.*;
import com.nCov.DataView.model.request.*;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.*;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.xmlbeans.impl.xb.xsdschema.All;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private StudentInformationDOMapper studentInformationDOMapper;

    @Resource
    private RouteCalDOMapper routeCalDOMapper;

    @Resource
    private ImpAreaDOMapper impAreaDOMapper;

    @Resource
    private FixTool fixTool;

    @Resource
    private PassInfoDOMapper passInfoDOMapper;

    @Resource
    private PathInfoDOMapper pathInfoDOMapper;

    @Resource
    private BaiduTool baiduTool;

    /**
     * @Description: 获取地区信息
     * @Param: [areaName]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/21
     */
    @Override
    public Result areaInfo(AreaInfoRequest areaInfoRequest) {
        try {
            CovDataExample covDataExample = new CovDataExample();
            covDataExample.createCriteria()
                .andProvincenameLike(areaInfoRequest.getProvinceName() + "%")
                    .andAreanameLike(areaInfoRequest.getCityName() + "%")
                    .andIsprovinceEqualTo(0);
            covDataExample.setOrderByClause("date DESC");
            List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
            if (covDataList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "查询数据有误");
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(TimeTool.stringToDay(areaInfoRequest.getDate()));
            CovData getCovData = null;
            for (CovData covData : covDataList) {
                if (!covData.getDate().before(calendar.getTime())) {
                    getCovData = covData;
                }
            }
            if (getCovData == null) {
                throw new AllException(EmAllException.BAD_REQUEST, "数据不存在");
            }

            AreaInfoResponse areaInfoResponse = new AreaInfoResponse();
            BeanUtils.copyProperties(getCovData, areaInfoResponse);
            List<AreaDO> areaDOList = areaDOMapper.mapSelectByName(areaInfoRequest.getCityName() + "%");
            if (areaDOList.size() > 1) {
                throw new AllException(EmAllException.DATABASE_ERROR, "查询数据有误");
            }
            areaInfoResponse.Calculation(areaDOList.size() == 1 ? areaDOList.get(0).getPopulation() : 0);
            return ResultTool.success(areaInfoResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (ParseException e) {
            log.error(e.getMessage());
            return ResultTool.error(500, "日期格式错误！");
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
            Map<Integer, AreaDO> provinceMap = areaDOMapper.getProvinceMapInt();
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
                        if (travel.getAreaname().contains(key))
                            covData = travel;
                    }
                    if (covData == null) return;
                    else {
                        BeanUtils.copyProperties(covData, areaInfoResponse);
                        areaInfoResponse.setAreaname(value.getName());
                    }
                } else {
                    BeanUtils.copyProperties(covDataMap.get(key), areaInfoResponse);
                    areaInfoResponse.setAreaname(value.getName());
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
            Map<Integer, AreaDO> provinceMap = areaDOMapper.getProvinceMapInt();
            List<AreaDO> areaDOList = areaDOMapper.nameLike(name);
            if (areaDOList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "暂无地区数据");
            } else if (areaDOList.size() > 1) {
                throw new AllException(EmAllException.DATABASE_ERROR, "地区名称重复，请精确查找");
            }

            CovDataExample covDataExample = new CovDataExample();
            covDataExample.createCriteria()
                    .andProvincenameLike(provinceMap.get(areaDOList.get(0).getParentid()).getName() + "%")
                    .andAreanameLike(name + "%")
                    .andIsprovinceEqualTo(0);
            covDataExample.setOrderByClause("date ASC");
            List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
            if (covDataList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "搜索地区暂无疫情数据");
            }

            Calendar calendarNeed = new GregorianCalendar();
            calendarNeed.set(2020, Calendar.JANUARY, 24, 0, 0, 0);

            DateInfoResponse dateInfoResponseTemp = new DateInfoResponse();
            dateInfoResponseTemp.setProvincename(provinceMap.get(areaDOList.get(0).getParentid()).getName());
            dateInfoResponseTemp.setAreaname(areaDOList.get(0).getName());
            dateInfoResponseTemp.setTotalconfirm(0);
            dateInfoResponseTemp.setTotaldead(0);
            dateInfoResponseTemp.setTotalsuspect(0);
            dateInfoResponseTemp.setTotalheal(0);
            dateInfoResponseTemp.setIsReal(false);
            dateInfoResponseTemp.Calculation(areaDOList.get(0).getPopulation());
            boolean begin = true;

            List<DateInfoResponse> dateInfoResponseList = new ArrayList<>();
            for (CovData covData : covDataList) {
                DateInfoResponse dateInfoResponse = new DateInfoResponse();
                BeanUtils.copyProperties(covData, dateInfoResponse);
                dateInfoResponse.Calculation(areaDOList.get(0).getPopulation());
                dateInfoResponse.setDate(TimeTool.timeToDaySy(covData.getDate()));
                dateInfoResponse.setIsReal(true);

                if (begin && TimeTool.dayDiffDate(covData.getDate(), calendarNeed.getTime()) < 0) {
                    while (TimeTool.dayDiffDate(covData.getDate(), calendarNeed.getTime()) < 0) {
                        dateInfoResponseTemp.setDate(TimeTool.timeToDaySy(calendarNeed.getTime()));
                        dateInfoResponseList.add(DateInfoResponse.objectCopy(dateInfoResponseTemp));
                        calendarNeed.add(Calendar.DATE, 1);
                    }
                } else if (!begin && TimeTool.dayDiffDate(covData.getDate(), calendarNeed.getTime()) < 0) {
                    while (TimeTool.dayDiffDate(covData.getDate(), calendarNeed.getTime()) < 0) {
                        DateInfoResponse dateInfoResponseEnd = DateInfoResponse.objectCopy(dateInfoResponseList.get(dateInfoResponseList.size() - 1));
                        dateInfoResponseEnd.setDate(TimeTool.timeToDaySy(calendarNeed.getTime()));
                        dateInfoResponseList.add(dateInfoResponseEnd);
                        calendarNeed.add(Calendar.DATE, 1);
                    }
                }
                dateInfoResponseList.add(dateInfoResponse);
                begin = false;
                calendarNeed.add(Calendar.DATE, 1);
            }

            Calendar calendarNow = Calendar.getInstance();
            while (TimeTool.dayDiffDate(calendarNow.getTime(), calendarNeed.getTime()) <= 0) {
                DateInfoResponse dateInfoResponseEnd = DateInfoResponse.objectCopy(dateInfoResponseList.get(dateInfoResponseList.size() - 1));
                dateInfoResponseEnd.setDate(TimeTool.timeToDaySy(calendarNeed.getTime()));
                dateInfoResponseList.add(dateInfoResponseEnd);
                calendarNeed.add(Calendar.DATE, 1);
            }
            return ResultTool.success(dateInfoResponseList);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (ParseException e) {
            log.error(e.getMessage());
            return ResultTool.error(500, "日期处理有误.");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 各地疫情严重程度分析
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/16
     */
    @Override
    public Result impDateInfo(String date) {
        try {
            return ResultTool.success(allAreaCal(date));
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 路程评估
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/26
     */
    @Override
    public Result routeCal(List<RouteCalRequest> routeCalRequestList) {
        if (routeCalRequestList.isEmpty()) {
            return ResultTool.error(500, "参数不能为空");
        }
        try {
            List<RouteCalReponse> resultList = new ArrayList<>();
            List<RouteCalDO> routeCalDOList = new ArrayList<>();
            List<CovRankResponse> allAreaRequestList = allAreaCal(TimeTool.timeToDaySy(new Date()));
            if (allAreaRequestList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "无地区评估数据");
            }

            for (RouteCalRequest routeCalRequest : routeCalRequestList) {
                RouteCalDOExample routeCalDataExample = new RouteCalDOExample();
                Calendar calendarToday = TimeTool.todayCreate();
                routeCalDataExample.createCriteria()
                        .andTypeEqualTo(routeCalRequest.getType())
                        .andStartEqualTo(routeCalRequest.getStart())
                        .andEndEqualTo(routeCalRequest.getEnd());

                List<RouteCalDO> routeCalDataList = routeCalDOMapper.selectByExample(routeCalDataExample);
                if (routeCalDataList.size() > 1) {
                    throw new AllException(EmAllException.DATABASE_ERROR, routeCalRequest.getStart() + "至" + routeCalRequest.getEnd() + ",交通方式为:" + ConstCorrespond.TRAN_TYPE[routeCalRequest.getType()] + ",时间为" + calendarToday.getTime());
                }

                //得到城市分数列表
                List<String> cityRequestList = new ArrayList<>(new HashSet<String>(routeCalRequest.getCitys()));
                List<CityCal> cityCalList = allAreaRequestList.stream()
                        .filter(covRankResponse -> {
                            for (String areaName : cityRequestList) {
                                if (areaName.contains(covRankResponse.getName()))
                                    return true;
                            }
                            return false;
                        })
                        .map(covRankResponse -> {
                            CityCal cityCal = new CityCal();
                            cityCal.setCityname(covRankResponse.getName());
                            cityCal.setCityscore((int) covRankResponse.getSumScore());
                            return cityCal;
                        }).collect(Collectors.toList());

                //有城市未找到信息，进行错误处理
                if (cityCalList.size() < cityRequestList.size()) {
                    StringBuilder stringBuilder = new StringBuilder("以下城市信息未找到");
                    for (String str : cityRequestList) {
                        int flag = 0;
                        for (CityCal cityCal : cityCalList) {
                            if (cityCal.getCityname().contains(fixTool.areaUni(str))) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) stringBuilder.append(str).append(",");
                    }
                    throw new AllException(EmAllException.DATABASE_ERROR,
                            stringBuilder.substring(0, stringBuilder.length() - 1));
                }

                //找到当前城市信息
                if (routeCalDataList.size() == 1) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    routeCalReponse.setTime(routeCalDataList.get(0).getTime());
                    routeCalReponse.setFinalscore(routeCalDataList.get(0).getScore());
                    routeCalReponse.setCity(cityCalList);
                    resultList.add(routeCalReponse);
                } else if (routeCalDataList.isEmpty()) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    RouteCalDO routeCalDO = new RouteCalDO();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    BeanUtils.copyProperties(routeCalRequest, routeCalDO);
                    DoubleSummaryStatistics sum = cityCalList.stream().mapToDouble(CityCal::getCityscore).summaryStatistics();

                    //准备插入数据库的数据
                    double time = routeCalRequest.getDistance() / 1000.0 / ConstCorrespond.SPEED[routeCalRequest.getType()];
                    routeCalDO.setTime(TimeTool.timeSlotToString(time));
                    routeCalDO.setDate(new Date());
                    routeCalDO.setScore(
                            (ConstCorrespond.ROUTE_WEIGHT[0] * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 24 ? 100 : (time / 24.0 * 100))
                                    + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[3] * sum.getAverage()) / 20.0
                    );
                    routeCalDO.setId(null);

                    routeCalDOList.add(routeCalDO);
                    routeCalReponse.setTime(routeCalDO.getTime());
                    routeCalReponse.setFinalscore(routeCalDO.getScore());
                    routeCalReponse.setCity(cityCalList);
                    resultList.add(routeCalReponse);
                }
            }
            if (!routeCalDOList.isEmpty()) {
                routeCalDOMapper.insertList(routeCalDOList);
            }

            SumCalResponse sumCalResponse = new SumCalResponse();
            sumCalResponse.setResultList(resultList);

            int type = 0;
            for (RouteCalRequest findMainType : routeCalRequestList) {
                if (findMainType.getType() > type) type = findMainType.getType();
            }
            sumCalResponse.setType(ConstCorrespond.TRAN_TYPE[type]);
            sumCalResponse.setSumScore(NumberTool.doubleToStringWotH(resultList.stream().mapToDouble(RouteCalReponse::getFinalscore).average().getAsDouble()));
            return ResultTool.success(sumCalResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 同一终点和起点的不同路径进行计算
     * @Param: [PathInfoDO]
     * @return:
     * @Author: pongshy
     * @Date: 2020/3/29
     */
    @Override
    public List<SumCalResponse> getAllRouteCal(List<PathInfoDO> pathInfoDOList) throws AllException {
        List<SumCalResponse> list = new ArrayList<>();
        List<RouteCalRequest> routeCalRequestList = new ArrayList<>();

        for (PathInfoDO pathInfoDO : pathInfoDOList) {
            //对每一条线路进行评估
            Integer pathId = pathInfoDO.getId();

            //order表示各个路段
            List<PassInfoDO> orderList = passInfoDOMapper.selectByPathId(pathId);

            if (orderList.size() == 0) {
                return null;
            }
            for (PassInfoDO temp : orderList) {
                PassInfoDOExample passInfoDOExample1 = new PassInfoDOExample();
                passInfoDOExample1.createCriteria()
                        .andPathIdEqualTo(pathId)
                        .andOrderIdEqualTo(temp.getOrderId());
                List<PassInfoDO> passInfoDOS = passInfoDOMapper.selectByExample(passInfoDOExample1);
                passInfoDOExample1.clear();

                if (passInfoDOS.size() == 0) {
                    continue;
                }
                PassInfoDO passInfoDO = passInfoDOS.get(0);

                RouteCalRequest routeCalRequest = new RouteCalRequest();
                List<String> cities = new ArrayList<>();
                for (PassInfoDO temp_city : passInfoDOS) {
                    cities.add(temp_city.getArea());
                }
                routeCalRequest.setCitys(cities);
                routeCalRequest.setDistance(passInfoDO.getDistance());
                routeCalRequest.setType(passInfoDO.getTypeNum());
                routeCalRequest.setTitle(passInfoDO.getTitle());
                routeCalRequest.setStart(passInfoDO.getStartAddress());
                routeCalRequest.setEnd(passInfoDO.getEndAddress());

                routeCalRequestList.add(routeCalRequest);

            }
            if (routeCalRequestList.isEmpty()) {
                return null;
            }
            SumCalResponse sumCalResponse = getRouteCal(routeCalRequestList);
            sumCalResponse.setType(ConstCorrespond.TRAN_TYPE[pathInfoDO.getMainType()]);
            sumCalResponse.setSumScore(sumCalResponse.getSumScore());
            sumCalResponse.setResultList(sumCalResponse.getResultList());

            list.add(sumCalResponse);
        }

        return list;
    }

    /**
     * @Description: 路线评估
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.info.SumCalResponse
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    @Override
    public SumCalResponse getRouteCal(List<RouteCalRequest> routeCalRequestList) {
        try {
            if (routeCalRequestList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "参数不能为空");
            }
            List<RouteCalReponse> resultList = new ArrayList<>();
            List<RouteCalDO> routeCalDOList = new ArrayList<>();
            List<CovRankResponse> allAreaRequestList = allAreaCal(TimeTool.timeToDaySy(new Date()));
            if (allAreaRequestList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "无地区评估数据");
            }

            for (RouteCalRequest routeCalRequest : routeCalRequestList) {
                RouteCalDOExample routeCalDataExample = new RouteCalDOExample();
                Calendar calendarToday = TimeTool.todayCreate();
                routeCalDataExample.createCriteria()
                        .andTypeEqualTo(routeCalRequest.getType())
                        .andStartEqualTo(routeCalRequest.getStart())
                        .andEndEqualTo(routeCalRequest.getEnd());

                List<RouteCalDO> routeCalDataList = routeCalDOMapper.selectByExample(routeCalDataExample);
                if (routeCalDataList.size() > 1) {
                    throw new AllException(EmAllException.DATABASE_ERROR, routeCalRequest.getStart() + "至" + routeCalRequest.getEnd() + ",交通方式为:" + ConstCorrespond.TRAN_TYPE[routeCalRequest.getType()] + ",时间为" + calendarToday.getTime());
                }

                //得到城市分数列表
                List<String> cityRequestList = new ArrayList<>(new HashSet<String>(routeCalRequest.getCitys()));
                List<CityCal> cityCalList = allAreaRequestList.stream()
                        .filter(covRankResponse -> {
                            for (String areaName : cityRequestList) {
                                if (areaName.contains(covRankResponse.getName()))
                                    return true;
                            }
                            return false;
                        })
                        .map(covRankResponse -> {
                            CityCal cityCal = new CityCal();
                            cityCal.setCityname(covRankResponse.getName());
                            cityCal.setCityscore((int) covRankResponse.getSumScore());
                            return cityCal;
                        }).collect(Collectors.toList());

                //有城市未找到信息，进行错误处理
                if (cityCalList.size() < cityRequestList.size()) {
                    StringBuilder stringBuilder = new StringBuilder("以下城市信息未找到");
                    for (String str : cityRequestList) {
                        int flag = 0;
                        for (CityCal cityCal : cityCalList) {
                            if (cityCal.getCityname().contains(fixTool.areaUni(str))) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) stringBuilder.append(str).append(",");
                    }
                    throw new AllException(EmAllException.DATABASE_ERROR,
                            stringBuilder.substring(0, stringBuilder.length() - 1));
                }

                //找到当前城市信息
                if (routeCalDataList.size() == 1) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    routeCalReponse.setTime(routeCalDataList.get(0).getTime());
                    routeCalReponse.setFinalscore(routeCalDataList.get(0).getScore());
                    routeCalReponse.setCity(cityCalList);
                    resultList.add(routeCalReponse);
                } else if (routeCalDataList.isEmpty()) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    RouteCalDO routeCalDO = new RouteCalDO();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    BeanUtils.copyProperties(routeCalRequest, routeCalDO);
                    DoubleSummaryStatistics sum = cityCalList.stream().mapToDouble(CityCal::getCityscore).summaryStatistics();

                    //准备插入数据库的数据
                    double time = routeCalRequest.getDistance() / 1000.0 / ConstCorrespond.SPEED[routeCalRequest.getType()];
                    routeCalDO.setTime(TimeTool.timeSlotToString(time));
                    routeCalDO.setDate(new Date());
                    routeCalDO.setScore(
                            (ConstCorrespond.ROUTE_WEIGHT[0] * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 24 ? 100 : (time / 24.0 * 100))
                                    + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[3] * sum.getAverage()) / 20.0
                    );
                    routeCalDO.setId(null);

                    routeCalDOList.add(routeCalDO);
                    routeCalReponse.setTime(routeCalDO.getTime());
                    routeCalReponse.setFinalscore(routeCalDO.getScore());
                    routeCalReponse.setCity(cityCalList);
                    resultList.add(routeCalReponse);
                }
            }
            if (!routeCalDOList.isEmpty()) {
                routeCalDOMapper.insertList(routeCalDOList);
            }

            SumCalResponse sumCalResponse = new SumCalResponse();
            sumCalResponse.setResultList(resultList);

            int type = 0;
            for (RouteCalRequest findMainType : routeCalRequestList) {
                if (findMainType.getType() > type) type = findMainType.getType();
            }
            sumCalResponse.setType(ConstCorrespond.TRAN_TYPE[type]);
            sumCalResponse.setSumScore(NumberTool.doubleToStringWotH(resultList.stream().mapToDouble(RouteCalReponse::getFinalscore).average().getAsDouble()));
            return sumCalResponse;
        } catch (AllException e) {
            log.error(e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * @Description: 前端发送地址信息，后端进行风险评估并返回分数
     * @Param: [AddressRequest]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    @Override
    public Result getAssessment(AddressRequest data) throws AllException, IOException {
        List<String> addressList = data.getAddressList();
        List<String> errorAddressList = new ArrayList<>();
        List<AssessmentAllResponse> list = new ArrayList<>();

        final String endAddress = "上海大学宝山校区";

        SumAssessmentResponse sumAssessmentResponse = new SumAssessmentResponse();
        PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
        for (String startAddress : addressList) {
            pathInfoDOExample.createCriteria().andStartEqualTo(startAddress);
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);
            pathInfoDOExample.clear();

            //如果数据库中存在
            if (pathInfoDOList.size() >= 1) {

                List<SumCalResponse> sumCalResponseList = getAllRouteCal(pathInfoDOList);

                AssessmentAllResponse assessmentAllResponse = new AssessmentAllResponse();
                assessmentAllResponse.setStart(startAddress);
                assessmentAllResponse.setEnd(endAddress);
                assessmentAllResponse.setSumCalResponseList(sumCalResponseList);

                list.add(assessmentAllResponse);
            } else {
                //如果数据库中不存在该地址信息
                try {
                    AssessmentAllResponse assessmentAllResponse = getScoreAndInsert(startAddress, startAddress, endAddress);

                    if (assessmentAllResponse.getSumCalResponseList().isEmpty()) {
                        errorAddressList.add(startAddress);
                    }
                    list.add(assessmentAllResponse);
                } catch (Exception e) {
                    try {
                        AssessmentAllResponse assessmentAllResponse = getScoreAndInsert(startAddress, startAddress.substring(0, startAddress.indexOf("路") + 1), endAddress);

                        if (assessmentAllResponse.getSumCalResponseList().isEmpty()) {
                            errorAddressList.add(startAddress);
                        }
                        list.add(assessmentAllResponse);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        log.info("地址：" + startAddress + "查询失败");
                        errorAddressList.add(startAddress);
                    }
                }
            }
        }
        sumAssessmentResponse.setAssessmentList(list);
        sumAssessmentResponse.setErrorAddress(errorAddressList);

        return ResultTool.success(sumAssessmentResponse);
    }

    /**
     * @Description: 使用百度api查询该地址回校路径，并插入数据库中
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    @Override
    public AssessmentAllResponse getScoreAndInsert(String startAddress, String start, String endAddress) throws AllException, IOException {
        PathRequest pathRequest = baiduTool.pathInfo(start, endAddress);

        AssessmentAllResponse assessmentAllResponse = new AssessmentAllResponse();
        assessmentAllResponse.setStart(startAddress);
        assessmentAllResponse.setEnd(endAddress);

        //n条路线
        List<RouteListRequest> routeListRequests = pathRequest.getPathList();

        PathInfoDO record = new PathInfoDO();
        record.setStart(startAddress);
        record.setEnd(endAddress);

        List<SumCalResponse> sumCalResponseList = new ArrayList<>();
        PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();

        //每一条路径信息，存入数据库
        for (RouteListRequest routeListRequest : routeListRequests) {
            //每一条路线
            int pathId = 0;
            int routeNum = 0;

            record.setMainType(routeNum);
            pathInfoDOMapper.insertSelective(record);
            pathInfoDOExample.createCriteria().andStartEqualTo(startAddress).andMainTypeEqualTo(routeNum);
            PathInfoDO pathInfoDO = pathInfoDOMapper.selectByExample(pathInfoDOExample).get(routeNum);
            pathId = pathInfoDO.getId();
            pathInfoDOExample.clear();

            SumCalResponse sumCalResponse = getRouteCal(routeListRequest.getRouteCalRequestList());

            int order_num = 0;
            int main_type = 0;
            for (RouteCalRequest routeCalRequest : routeListRequest.getRouteCalRequestList()) {
                List<PassInfoDO> passInfoDOList = new ArrayList<>();

                for (String city : routeCalRequest.getCitys()) {
                    PassInfoDO passInfoDO_record = new PassInfoDO();
                    passInfoDO_record.setArea(city);
                    passInfoDO_record.setDistance((int)routeCalRequest.getDistance());
                    passInfoDO_record.setOrderId(order_num);
                    passInfoDO_record.setStartAddress(routeCalRequest.getStart());
                    passInfoDO_record.setEndAddress(routeCalRequest.getEnd());
                    passInfoDO_record.setTitle(routeCalRequest.getTitle());
                    passInfoDO_record.setPathId(pathId);
                    passInfoDO_record.setTypeNum(routeCalRequest.getType());

                    passInfoDOList.add(passInfoDO_record);
                }
                if (routeCalRequest.getType() > main_type) {
                    main_type = routeCalRequest.getType();
                }
                passInfoDOMapper.insertList(passInfoDOList);
                ++order_num;
            }
            ++routeNum;
            pathInfoDO.setMainType(main_type);
            pathInfoDOMapper.updateByPrimaryKeySelective(pathInfoDO);
            sumCalResponseList.add(sumCalResponse);
        }
        assessmentAllResponse.setSumCalResponseList(sumCalResponseList);

        return assessmentAllResponse;
    }


    /**
     * @Description: 路径存储兼查询
     * @Param: [routeCalRequestList]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
    @Transactional
    @Override
    public Result routeStore(PathRequest pathRequest) {
        try {
            //待返回的数据
            PathResponse pathResponse = new PathResponse();
            pathResponse.setSumCalResponseList(new ArrayList<>());

            List<CovRankResponse> covRankResponseList = allAreaCal(TimeTool.timeToDaySy(new Date()));

            //待插入的途径地区
            List<PassInfoDO> passInfoDOList = new ArrayList<>();
            for (RouteListRequest routeListRequest : pathRequest.getPathList()) {
                //如果为type为-1,则知定义为空的元素，跳过
                if (routeListRequest.getRouteCalRequestList().size() == 1) {
                    if (routeListRequest.getRouteCalRequestList().get(0).getType() == -1) {
                        continue;
                    }
                }

                SumCalResponse sumCalResponse = new SumCalResponse();
                sumCalResponse.setResultList(new ArrayList<>());
                PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();

                int type = 0;
                for (RouteCalRequest findMainType : routeListRequest.getRouteCalRequestList()) {
                    if (findMainType.getType() > type) type = findMainType.getType();
                }

                sumCalResponse.setType(ConstCorrespond.TRAN_TYPE[type]);
                pathInfoDOExample.createCriteria()
                        .andStartEqualTo(routeListRequest.getRouteCalRequestList().get(0).getStartAdressZone())
//                            .andEndEqualTo("上海大学宝山校区")
                        .andMainTypeEqualTo(type);
                List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);

                //查询是否已经存在
                int exist = 1;
                PathInfoDO pathInfoDO = new PathInfoDO();
                if (pathInfoDOList.isEmpty()) {
                    pathInfoDO.setStart(routeListRequest.getRouteCalRequestList().get(0).getStartAdressZone());
                    pathInfoDO.setEnd("上海大学宝山校区");
                    pathInfoDO.setMainType(type);
                    exist = 0;
                    pathInfoDOMapper.insertSelective(pathInfoDO);
                }

                int number = 0;
                for (RouteCalRequest routeCalRequest : routeListRequest.getRouteCalRequestList()) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();

                    //算出总分
                    //得到城市分数列表
                    List<String> cityRequestList = new ArrayList<>(new HashSet<String>(routeCalRequest.getCitys()));
                    List<CityCal> cityCalList = covRankResponseList.stream()
                            .filter(covRankResponse -> {
                                for (String areaName : cityRequestList) {
                                    if (areaName.contains(covRankResponse.getName()))
                                        return true;
                                }
                                return false;
                            })
                            .map(covRankResponse -> {
                                CityCal cityCal = new CityCal();
                                cityCal.setCityname(covRankResponse.getName());
                                cityCal.setCityscore((int) covRankResponse.getSumScore());
                                return cityCal;
                            }).collect(Collectors.toList());

                    //赋值与计算
                    double time = routeCalRequest.getDistance() / 1000.0 / ConstCorrespond.SPEED[routeCalRequest.getType()];
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    routeCalReponse.setTime(TimeTool.timeSlotToString(time));
                    routeCalReponse.setCity(cityCalList);
                    routeCalReponse.setFinalscore(
                            (ConstCorrespond.ROUTE_WEIGHT[0] * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 24 ? 100 : (time / 24.0 * 100))
                                    + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[3] * cityCalList.stream().mapToDouble(CityCal::getCityscore).average().getAsDouble()) / 20.0
                    );
                    sumCalResponse.getResultList().add(routeCalReponse);

                    //准备插入数据
                    if (exist == 0) {
                        for (String area : cityRequestList) {
                            PassInfoDO passInfoDO = new PassInfoDO();
                            passInfoDO.setPathId(pathInfoDO.getId());
                            passInfoDO.setArea(area);
                            passInfoDO.setTypeNum(routeCalRequest.getType());
                            passInfoDO.setStartAddress(routeCalRequest.getStart());
                            passInfoDO.setEndAddress(routeCalRequest.getEnd());
                            passInfoDO.setTitle(routeCalReponse.getTitle());
                            passInfoDO.setOrderId(number);
                            passInfoDO.setDistance((int) routeCalRequest.getDistance());
                            passInfoDOList.add(passInfoDO);
                        }
                    }
                    number++;
                }
                sumCalResponse.setSumScore(NumberTool.doubleToStringWotH(sumCalResponse.getResultList().stream().mapToDouble(RouteCalReponse::getFinalscore).average().getAsDouble()));
                pathResponse.getSumCalResponseList().add(sumCalResponse);
            }

            if (!passInfoDOList.isEmpty()) {
                passInfoDOMapper.insertList(passInfoDOList);
            }
            return ResultTool.success(pathResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 使用excel表格导入学生信息
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/28
     */
    @Async
    @Override
    public Result excelIn(MultipartFile file) throws AllException, IOException {
        if (file == null) {
            return ResultTool.error(400, "上传文件为空");
        }

        System.out.println(file.getOriginalFilename());

        InputStream in = file.getInputStream();
        Sheet sheet = ImportExcel.getBankListByExcel(in, file.getOriginalFilename());

        for (int i = 1; i <= sheet.getLastRowNum(); ++i) {
            Row row =sheet.getRow(i);

            StudentInfo info = new StudentInfo();
            info.setCountry(row.getCell(9).getStringCellValue());
            info.setProvince(row.getCell(10).getStringCellValue());
            info.setCity(row.getCell(11).getStringCellValue());
            info.setArea(row.getCell(12).getStringCellValue());
            info.setAddress(row.getCell(13).getStringCellValue());
            info.setTransportion(row.getCell(30).getStringCellValue());

            StudentInformationDO studentInformation = new StudentInformationDO();
            BeanUtils.copyProperties(info, studentInformation);
            studentInformation.setId(i);

            try {
                if (studentInformationDOMapper.insertSelective(studentInformation) != 1) {
                    return ResultTool.error(500);
                }
            }
            catch (Exception e) {
                throw new AllException(EmAllException.DATABASE_ERROR);
            }
        }

        return ResultTool.success();
    }

    /**
     * @Description: 所有地区评估
     * @Param: [date]
     * @return: java.util.List<com.nCov.DataView.model.response.info.CovRankResponse>
     * @Author: SoCMo
     * @Date: 2020/3/18
     */
    @Transactional
    @Cacheable(value = "allAreaCal", key = "#date")
    public List<CovRankResponse> allAreaCal(String date) throws AllException, ParseException {
        ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
        impAreaDOExample.createCriteria().andDateEqualTo(TimeTool.stringToDay(date));
        List<ImpAreaDO> impAreaDOList = impAreaDOMapper.selectByExample(impAreaDOExample);
        if (!impAreaDOList.isEmpty()) {
            return impAreaDOList.stream().map(impAreaDO -> {
                CovRankResponse covRankResponse = new CovRankResponse();
                BeanUtils.copyProperties(impAreaDO, covRankResponse);
                covRankResponse.setProvincename(impAreaDO.getProvinceName());
                covRankResponse.setAllRank(impAreaDO.getAllrank());
                return covRankResponse;
            }).collect(Collectors.toList());
        }

        Map<String, AreaDO> cityMap = areaDOMapper.getCityMap();
        if (cityMap.isEmpty()) {
            throw new AllException(EmAllException.DATABASE_ERROR, "地区数据为空");
        }
        Map<Integer, AreaDO> provinceMap = areaDOMapper.getProvinceMapInt();

        if (TimeTool.stringToDay(date).before(TimeTool.stringToDay("2020-01-24"))) {
            throw new AllException(EmAllException.DATABASE_ERROR, "无法提供早于1月24日的评估");
        }

        //获取今日数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(TimeTool.stringToDay(date));
        Calendar calendarTemp = Calendar.getInstance();
        calendarTemp.setTime(calendar.getTime());

        Map<String, CovData> covDataMapNow = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendar.getTime()));
        //找到有数据的那天，添加
        if (covDataMapNow.isEmpty()) {
            while (covDataMapNow.isEmpty() && !calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                calendarTemp.add(Calendar.DATE, -1);
                covDataMapNow = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendarTemp.getTime()));
            }
            if (calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                throw new AllException(EmAllException.DATABASE_ERROR, "暂无疫情数据");
            }
            for (CovData covData : covDataMapNow.values()) {
                covData.setId(null);
                covData.setDate(calendar.getTime());
            }
            covDataMapper.insertList(new ArrayList<>(covDataMapNow.values()));
        }

        calendar.add(Calendar.DATE, -13);
        //防止日期在1月24日前
        while (calendar.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
            calendar.add(Calendar.DATE, 1);
        }

        calendarTemp.setTime(calendar.getTime());
        Map<String, CovData> covDataMapThr = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendar.getTime()));
        //找到有数据的那天，添加
        if (covDataMapThr.isEmpty()) {
            while (covDataMapThr.isEmpty() && !calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                calendarTemp.add(Calendar.DATE, -1);
                covDataMapThr = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendarTemp.getTime()));
            }
            if (calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                throw new AllException(EmAllException.DATABASE_ERROR, "暂无疫情数据");
            }
            for (CovData covData : covDataMapThr.values()) {
                covData.setId(null);
                covData.setDate(calendar.getTime());
            }
            covDataMapper.insertList(new ArrayList<>(covDataMapThr.values()));
        }

        for (AreaDO areaDO : cityMap.values()) {
            ImpAreaDO impAreaDO = new ImpAreaDO();
            BeanUtils.copyProperties(areaDO, impAreaDO);
            impAreaDO.setProvinceName(provinceMap.get(areaDO.getParentid()).getName());

            CovData covDataNow;
            CovData covDataThr;
            if ((covDataNow = covDataMapNow.get(areaDO.getName())) == null) {
                List<CovData> listNow = covDataMapNow.values()
                        .stream()
                        .filter(covData -> covData.getAreaname().contains(areaDO.getName()))
                        .collect(Collectors.toList());
                if (listNow.isEmpty()) {
                    if ((covDataNow = fixTool.fixCovDate(date, areaDO.getName())) == null) {
                        continue;
                    }
                } else {
                    covDataNow = listNow.get(0);
                }
            }

            if ((covDataThr = covDataMapThr.get(areaDO.getName())) == null) {
                List<CovData> listThr = covDataMapThr.values()
                        .stream()
                        .filter(covData -> covData.getAreaname().contains(areaDO.getName()))
                        .collect(Collectors.toList());
                if (listThr.size() != 1) {
                    if ((covDataThr = fixTool.fixCovDate(TimeTool.timeToDaySy(calendar.getTime()), areaDO.getName())) == null) {
                        continue;
                    }
                } else {
                    covDataThr = listThr.get(0);
                }
            }
            double RemainConfirm = Double.parseDouble(NumberTool.doubleToStringWotH(NumberTool.intDivision(covDataNow.getTotalconfirm() - covDataNow.getTotaldead() - covDataNow.getTotalheal(), areaDO.getPopulation()) * 1000000));
            impAreaDO.setRemainConfirm(Math.max(RemainConfirm, 0));
            int count = covDataNow.getTotalconfirm() - covDataNow.getTotalheal() - covDataNow.getTotaldead();
            impAreaDO.setRemainCount(Math.max(count, 0));
            int growth = covDataNow.getTotalconfirm() - covDataThr.getTotalconfirm();
            impAreaDO.setGrowth(Math.max(growth, 0));
            impAreaDOList.add(impAreaDO);
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getRemainConfirm).reversed());
        int number = 1;
        double last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getRemainConfirm();
                impAreaDOList.get(0).setRemainConfirmRank(number++);
            } else if (last == impAreaDOList.get(i).getRemainConfirm()) {
                impAreaDOList.get(i).setRemainConfirmRank(impAreaDOList.get(i - 1).getRemainConfirmRank());
                number++;
            } else {
                last = impAreaDOList.get(i).getRemainConfirm();
                impAreaDOList.get(i).setRemainConfirmRank(number++);
            }
        }
        int lastRank = impAreaDOList.get(impAreaDOList.size() - 1).getRemainConfirmRank();
        for (ImpAreaDO covRank : impAreaDOList) {
            covRank.setRemainScore(NumberTool.Score(covRank.getRemainConfirmRank(), lastRank));
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getRemainCount).reversed());
        number = 1;
        last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getRemainCount();
                impAreaDOList.get(0).setRemainCountRank(number++);
            } else if (last == impAreaDOList.get(i).getRemainCount()) {
                impAreaDOList.get(i).setRemainCountRank(impAreaDOList.get(i - 1).getRemainCountRank());
                number++;
            } else {
                last = impAreaDOList.get(i).getRemainCount();
                impAreaDOList.get(i).setRemainCountRank(number++);
            }
        }
        for (ImpAreaDO impAreaDO : impAreaDOList) {
            impAreaDO.setRemainCountScore(NumberTool.Score(impAreaDO.getRemainCountRank(), impAreaDOList.get(impAreaDOList.size() - 1).getRemainCountRank()));
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getGrowth).reversed());
        number = 1;
        last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getGrowth();
                impAreaDOList.get(0).setGrowthRank(number++);
            } else if (last == impAreaDOList.get(i).getGrowth()) {
                impAreaDOList.get(i).setGrowthRank(impAreaDOList.get(i - 1).getGrowthRank());
                number++;
            } else {
                last = impAreaDOList.get(i).getGrowth();
                impAreaDOList.get(i).setGrowthRank(number++);
            }
        }

        for (ImpAreaDO impAreaDO : impAreaDOList) {
            impAreaDO.setGrowthScore(NumberTool.Score(impAreaDO.getGrowthRank(), impAreaDOList.get(impAreaDOList.size() - 1).getGrowthRank()));
            impAreaDO.setSumScore(impAreaDO.getRemainCountScore() * 0.3 + impAreaDO.getRemainScore() * 0.3 + impAreaDO.getGrowthScore() * 0.4);
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getSumScore).reversed());
        number = 1;
        last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getSumScore();
                impAreaDOList.get(0).setAllrank(number++);
            } else if (last == impAreaDOList.get(i).getSumScore()) {
                impAreaDOList.get(i).setAllrank(impAreaDOList.get(i - 1).getAllrank());
                number++;
            } else {
                last = impAreaDOList.get(i).getSumScore();
                impAreaDOList.get(i).setAllrank(number++);
            }
        }
        for (ImpAreaDO impAreaDO : impAreaDOList) {
            impAreaDO.setDate(TimeTool.stringToDay(date));
        }
        impAreaDOMapper.insertList(impAreaDOList);

        return impAreaDOList.stream().map(impAreaDO -> {
            CovRankResponse covRankResponse = new CovRankResponse();
            BeanUtils.copyProperties(impAreaDO, covRankResponse);
            covRankResponse.setProvincename(impAreaDO.getProvinceName());
            covRankResponse.setAllRank(impAreaDO.getAllrank());
            return covRankResponse;
        }).collect(Collectors.toList());
    }


}
