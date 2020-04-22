package com.nCov.DataView.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * program: EpidemicImpl
 * description: 疫情地图
 * author: SoCMo
 * create: 2020/2/21
 */
@Slf4j
@Service
@EnableScheduling
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
    private AbroadInputDOMapper abroadInputDOMapper;

    @Resource
    private FixTool fixTool;

    @Resource
    private PassInfoDOMapper passInfoDOMapper;

    @Resource
    private PathInfoDOMapper pathInfoDOMapper;

    @Resource
    private BaiduTool baiduTool;

    @Resource
    private AssessDOMapper assessDOMapper;

    private final static Integer lock = 1;

    private final static Integer AbroadLock = 1;

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

                //找到当前城市信息
                if (routeCalDataList.size() >= 1) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    routeCalReponse.setTime(routeCalDataList.get(0).getTime());
                    routeCalReponse.setFinalscore(routeCalDataList.get(0).getScore());
                    double time = TimeTool.stringToHour(routeCalDataList.get(0).getTime());
                    routeCalReponse.setTimeScore(NumberTool.doubleToStringWotH(time >= 6 ? 100 : (time / 6.0 * 100)));
                    routeCalReponse.setTransportScore(
                            NumberTool.doubleToStringWotH(
                                    ConstCorrespond.ROUTE_WEIGHT[0] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CROWD[routeCalDataList.get(0).getType()]
                                            + ConstCorrespond.ROUTE_WEIGHT[2] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CLEAN_SCORE[routeCalDataList.get(0).getType()]
                            )
                    );
                    routeCalReponse.setCity(cityCalList);
                    resultList.add(routeCalReponse);
                } else if (routeCalDataList.isEmpty()) {
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    RouteCalDO routeCalDO = new RouteCalDO();
                    BeanUtils.copyProperties(routeCalRequest, routeCalReponse);
                    BeanUtils.copyProperties(routeCalRequest, routeCalDO);
                    double max = cityCalList.stream().mapToDouble(CityCal::getCityscore).max().getAsDouble();

                    //准备插入数据库的数据
                    double time = routeCalRequest.getDistance() / 1000.0 / ConstCorrespond.SPEED[routeCalRequest.getType()];
                    routeCalDO.setTime(TimeTool.timeSlotToString(time));
                    routeCalDO.setDate(new Date());
                    routeCalDO.setScore(
                            ConstCorrespond.ROUTE_WEIGHT[0] * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 6 ? 100 : (time / 6 * 100))
                                    + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[3] * max
                    );
                    routeCalDO.setId(null);

                    routeCalDOList.add(routeCalDO);
                    routeCalReponse.setTimeScore(NumberTool.doubleToStringWotH(time >= 6 ? 100 : (time / 6.0 * 100)));
                    routeCalReponse.setTransportScore(
                            NumberTool.doubleToStringWotH(
                                    ConstCorrespond.ROUTE_WEIGHT[0] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                            + ConstCorrespond.ROUTE_WEIGHT[2] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                            )
                    );
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
            sumCalResponse.setSumScore(NumberTool.doubleToStringWotH(resultList.stream().mapToDouble(RouteCalReponse::getFinalscore).max().getAsDouble()));
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
    public List<SumCalResponse> getAllRouteCal(List<PathInfoDO> pathInfoDOList) throws AllException, ParseException {
        List<SumCalResponse> list = new ArrayList<>();

        for (PathInfoDO pathInfoDO : pathInfoDOList) {
            List<RouteCalRequest> routeCalRequestList = new ArrayList<>();
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
            SumCalResponse sumCalResponse = calculate(routeCalRequestList);

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
                                    + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 12 ? 100 : (time / 12.0 * 100))
                                    + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[3] * sum.getAverage()) / 20.0
                    );
                    routeCalDO.setId(null);

                    routeCalDOList.add(routeCalDO);
                    routeCalReponse.setTime(routeCalDO.getTime());
                    routeCalReponse.setFinalscore(routeCalDO.getScore());
                    routeCalReponse.setCity(cityCalList);
                    routeCalReponse.setTimeScore(NumberTool.doubleToStringWotH(time >= 12 ? 100 : (time / 12.0 * 100)));
                    routeCalReponse.setTransportScore(
                            NumberTool.doubleToStringWotH(
                                    ConstCorrespond.ROUTE_WEIGHT[0] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                            + ConstCorrespond.ROUTE_WEIGHT[2] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                            * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                            )
                    );

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
    public Result getAssessment(AddressRequest data) throws AllException, IOException, ParseException {
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

                    if (assessmentAllResponse == null || assessmentAllResponse.getSumCalResponseList().isEmpty()) {
                        errorAddressList.add(startAddress);
                    }
                    list.add(assessmentAllResponse);
                } catch (Exception e) {
                    try {
                        String start_temp = null;
                        if (startAddress.contains("路")) {
                            start_temp = startAddress.substring(0, startAddress.indexOf("路") + 1);
                        }
                        else {
                            if (startAddress.contains("区")) {
                                start_temp = startAddress.substring(0, startAddress.indexOf("路") + 1);
                            }
                            else {
                                start_temp = startAddress.substring(0, startAddress.indexOf("区") + 1);
                            }
                        }

                        AssessmentAllResponse assessmentAllResponse = getScoreAndInsert(startAddress, start_temp, endAddress);

                        if (assessmentAllResponse == null || assessmentAllResponse.getSumCalResponseList().isEmpty()) {
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
    public AssessmentAllResponse getScoreAndInsert(String startAddress, String start, String endAddress) throws AllException, IOException, ParseException {
//        PathRequest pathRequest = baiduTool.pathInfo(start, endAddress);
//        //n条路线
//        List<RouteListRequest> routeListRequests = pathRequest.getPathList();
//        if (routeListRequests.isEmpty()) {
//            return null;
//        }
//
//        AssessmentAllResponse assessmentAllResponse = new AssessmentAllResponse();
//        assessmentAllResponse.setStart(startAddress);
//        assessmentAllResponse.setEnd(endAddress);
//
//        List<SumCalResponse> sumCalResponseList = new ArrayList<>();
//        PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
//
//        //每一条路径信息，存入数据库
//        int routeNum = 0;
//        for (RouteListRequest routeListRequest : routeListRequests) {
//            //每一条路线
//            int pathId = 0;
//            PathInfoDO record = new PathInfoDO();
//
//            record.setStart(startAddress);
//            record.setEnd(endAddress);
//            record.setMainType(routeNum);
//            pathInfoDOMapper.insertSelective(record);
//
//            pathInfoDOExample.createCriteria().andStartEqualTo(startAddress).andMainTypeEqualTo(routeNum);
//            PathInfoDO pathInfoDO = pathInfoDOMapper.selectByExample(pathInfoDOExample).get(0);
//            pathId = pathInfoDO.getId();
//            pathInfoDOExample.clear();
//
//            SumCalResponse sumCalResponse = calculate(routeListRequest.getRouteCalRequestList());
//
//            int order_num = 0;
//            int main_type = 0;
//            for (RouteCalRequest routeCalRequest : routeListRequest.getRouteCalRequestList()) {
//                List<PassInfoDO> passInfoDOList = new ArrayList<>();
//
//                for (String city : routeCalRequest.getCitys()) {
//                    PassInfoDO passInfoDO_record = new PassInfoDO();
//                    passInfoDO_record.setArea(city);
//                    passInfoDO_record.setDistance((int)routeCalRequest.getDistance());
//                    passInfoDO_record.setOrderId(order_num);
//                    passInfoDO_record.setStartAddress(routeCalRequest.getStart());
//                    passInfoDO_record.setEndAddress(routeCalRequest.getEnd());
//                    passInfoDO_record.setTitle(routeCalRequest.getTitle());
//                    passInfoDO_record.setPathId(pathId);
//                    passInfoDO_record.setTypeNum(routeCalRequest.getType());
//
//                    passInfoDOList.add(passInfoDO_record);
//                }
//                if (routeCalRequest.getType() > main_type) {
//                    main_type = routeCalRequest.getType();
//                }
//                passInfoDOMapper.insertList(passInfoDOList);
//                ++order_num;
//            }
//            ++routeNum;
//            pathInfoDO.setMainType(main_type);
//            pathInfoDOMapper.updateByPrimaryKeySelective(pathInfoDO);
//
//            sumCalResponse.setType(ConstCorrespond.TRAN_TYPE[main_type]);
//            sumCalResponseList.add(sumCalResponse);
//        }
//        assessmentAllResponse.setSumCalResponseList(sumCalResponseList);
//
//        return assessmentAllResponse;
        return null;
    }

    /**
     * @Description: 传入路径信息，计算分数
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/3/31
     */
    @Override
    public SumCalResponse calculate(List<RouteCalRequest> routeCalRequestList) throws AllException, ParseException {
        SumCalResponse sumCalResponse = new SumCalResponse();
        sumCalResponse.setResultList(new ArrayList<>());
        List<CovRankResponse> covRankResponseList = allAreaCal(TimeTool.timeToDaySy(new Date()));
        for (RouteCalRequest routeCalRequest : routeCalRequestList) {

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
            routeCalReponse.setTimeScore(NumberTool.doubleToStringWotH(time >= 12 ? 100 : (time / 12.0 * 100)));
            routeCalReponse.setTransportScore(
                    NumberTool.doubleToStringWotH(
                            ConstCorrespond.ROUTE_WEIGHT[0] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                    * ConstCorrespond.CROWD[routeCalRequest.getType()]
                                    + ConstCorrespond.ROUTE_WEIGHT[2] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2])
                                    * ConstCorrespond.CLEAN_SCORE[routeCalRequest.getType()]
                    )
            );
            sumCalResponse.getResultList().add(routeCalReponse);
            sumCalResponse.setSumScore(NumberTool.doubleToStringWotH(sumCalResponse.getResultList().stream().mapToDouble(RouteCalReponse::getFinalscore).average().getAsDouble()));
        }

        return sumCalResponse;
    }

    /**
     * @Description: 读取数据库中存储的信息
     * @Param: [cur, nums]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    @Override
    public Result getSpecifiedNumber(Integer cur, Integer nums, String provinceNamae) throws AllException, ParseException {
        List<AssessmentAllResponse> list = new ArrayList<>();
        final String endAddress = "上海大学宝山校区";
        ReadFromDBResponse readFromDBResponse = new ReadFromDBResponse();
        try {
            PageHelper.startPage(cur, nums);
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectDistinctByStart("%" + provinceNamae + "%");
            PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();

            for (PathInfoDO pathInfoDO : pathInfoDOList) {
                String startAddress = pathInfoDO.getStart();

                pathInfoDOExample.createCriteria().andStartEqualTo(startAddress);
                List<PathInfoDO> pathInfoDOS = pathInfoDOMapper.selectByExample(pathInfoDOExample);
                pathInfoDOExample.clear();

                if (pathInfoDOS.size() >= 1) {
                    List<SumCalResponse> sumCalResponseList = getAllRouteCal(pathInfoDOS);
                    AssessmentAllResponse assessmentAllResponse = new AssessmentAllResponse();
                    assessmentAllResponse.setSumCalResponseList(sumCalResponseList);
                    assessmentAllResponse.setStart(startAddress);
                    assessmentAllResponse.setEnd(endAddress);

                    list.add(assessmentAllResponse);
                } else {
                    throw new AllException(EmAllException.DATABASE_ERROR);
                }
            }

            PageInfo pageInfo = new PageInfo(pathInfoDOList);
            int total = (int)pageInfo.getTotal();
            readFromDBResponse.setTotal(total);
            readFromDBResponse.setAssessmentAllResponseList(list);

            return ResultTool.success(readFromDBResponse);
        }
        catch (Exception e) {
            throw new AllException(EmAllException.DATABASE_ERROR);
        }
    }

    /**
     * @Description: 读取excel表格中的信息，并存储到数据库中
     * @Param: [file]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/4
     */
    @Async
    @Override
    public Result setInDataBase(MultipartFile file) throws AllException, IOException {
//        List<String> errorList = new ArrayList<>();
//        if (file == null) {
//            throw new AllException(EmAllException.BAD_REQUEST, "上传文件为空");
//        }
//        final String endAddress = "上海大学宝山校区";
//        InputStream in = file.getInputStream();
//        Sheet sheet = ImportExcel.getBankListByExcel(in, file.getOriginalFilename());
//
//        PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
//        for (int i = 9362; i <= sheet.getLastRowNum(); ++i) {
//            Row row = sheet.getRow(i);
//            String tempAddress = "";
//            StringBuilder temp_start = new StringBuilder();
//            String startAddress = "";
//
//            if (!row.getCell(9).getStringCellValue().equals("中国")) {
//                continue;
//            }
//
//            tempAddress = row.getCell(9).getStringCellValue()
//                         + row.getCell(10).getStringCellValue()
//                         + row.getCell(11).getStringCellValue()
//                         + row.getCell(12).getStringCellValue()
//                         + row.getCell(13).getStringCellValue();
//            tempAddress.replaceAll("\\s*", "");
//            //使用正则去除空格和非法字符
//            String regex = "[a-zA-Z0-9\\u4e00-\\u9fa5-]";
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(tempAddress);
//            while (matcher.find()) {
//                temp_start.append(matcher.group());
//            }
//            startAddress = temp_start.toString();
//            pathInfoDOExample.createCriteria().andStartEqualTo(startAddress);
//            int num = pathInfoDOMapper.countByExample(pathInfoDOExample);
//            pathInfoDOExample.clear();
//
//            if (num == 0) {
//                try {
//                    PathRequest pathRequest = baiduTool.pathInfo(startAddress, endAddress);
//                    //n条路线
//                    List<RouteListRequest> routeListRequests = pathRequest.getPathList();
//                    if (routeListRequests.isEmpty()) {
//                        return null;
//                    }
//                    //每一条路径信息，存入数据库
//                    for (RouteListRequest routeListRequest : routeListRequests) {
//                        //每一条路线
//                        int pathId = 0;
//                        PathInfoDO record = new PathInfoDO();
//
//                        record.setStart(startAddress);
//                        record.setEnd(endAddress);
//                        record.setMainType(-1);
//                        pathInfoDOMapper.insertSelective(record);
//                        pathId = record.getId();
//
//                        int order_num = 0;
//                        int main_type = 0;
//                        for (RouteCalRequest routeCalRequest : routeListRequest.getRouteCalRequestList()) {
//                            List<PassInfoDO> passInfoDOList = new ArrayList<>();
//
//                            for (String city : routeCalRequest.getCitys()) {
//                                PassInfoDO passInfoDO_record = new PassInfoDO();
//                                passInfoDO_record.setArea(city);
//                                passInfoDO_record.setDistance((int) routeCalRequest.getDistance());
//                                passInfoDO_record.setOrderId(order_num);
//                                passInfoDO_record.setStartAddress(routeCalRequest.getStart());
//                                passInfoDO_record.setEndAddress(routeCalRequest.getEnd());
//                                passInfoDO_record.setTitle(routeCalRequest.getTitle());
//                                passInfoDO_record.setPathId(pathId);
//                                passInfoDO_record.setTypeNum(routeCalRequest.getType());
//
//                                passInfoDOList.add(passInfoDO_record);
//                            }
//                            if (routeCalRequest.getType() > main_type) {
//                                main_type = routeCalRequest.getType();
//                            }
//                            passInfoDOMapper.insertList(passInfoDOList);
//                            ++order_num;
//                        }
//                        record.setMainType(main_type);
//                        pathInfoDOMapper.updateByPrimaryKeySelective(record);
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                    log.info("地址:" + startAddress + "无法读入数据库中");
//                    errorList.add(startAddress);
//                    System.out.println(errorList.size());
//                    System.out.println(startAddress);
//                }
//            }
//        }
//        System.out.println("finish!");
//        return ResultTool.success(errorList);
        return null;
    }

    /**
     * @Description: 单个城市风险查询
     * @Param: [city]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/8
     */
    @Override
    public Result cityQuery(CityRiskRequest cityRiskRequest) {
        try {
            ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
            impAreaDOExample.createCriteria()
                    .andProvinceNameLike(fixTool.provinceUni(cityRiskRequest.getProvince()) + "%")
                    .andNameLike(fixTool.areaUni(cityRiskRequest.getName()) + "%")
                    .andDateEqualTo(TimeTool.stringToDay(cityRiskRequest.getDate()));
            List<ImpAreaDO> impAreaDOList = impAreaDOMapper.selectByExample(impAreaDOExample);
            if (impAreaDOList.isEmpty()) {
                allAreaCal(cityRiskRequest.getDate());
                impAreaDOList = null;
                impAreaDOList = impAreaDOMapper.selectByExample(impAreaDOExample);
            }
            if (impAreaDOList.size() != 1) {
                throw new AllException(EmAllException.DATABASE_ERROR, cityRiskRequest.getProvince() + cityRiskRequest.getName() + "的" + cityRiskRequest.getDate() + "数据有误");
            }
            return ResultTool.success(impAreaDOList.get(0));
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(500, e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 返校风险评估
     * @Param: [pathQueryRequest]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/10
     */
    @Override
    public Result pathQuery(PathQueryRequest pathQueryRequest) {
        try {
            List<AssessDO> pathIdList = assessDOMapper.selectMax(TimeTool.timeToDaySy(TimeTool.todayCreate().getTime()), pathQueryRequest.getIndex(), pathQueryRequest.getNum(), fixTool.provinceUni(pathQueryRequest.getProvince()));
            if (pathIdList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "风险数据为空");
            }

            PathQueryListResponse pathQueryListResponse = new PathQueryListResponse();

            //查询PathInfo表
            PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
            pathInfoDOExample.createCriteria()
                    .andIdIn(pathIdList.stream().map(AssessDO::getPathId).collect(Collectors.toList()));
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);
            Map<Integer, PathInfoDO> pathInfoDOMap = pathInfoDOList.stream().collect(Collectors.toMap(PathInfoDO::getId, pathInfoDO -> pathInfoDO));

            pathQueryListResponse.setPathQueryResponseList(pathIdList.stream().map(assessDO -> {
                PathQueryResponse pathQueryResponse = new PathQueryResponse();
                PathInfoDO pathInfoDO = pathInfoDOMap.get(assessDO.getPathId());

                pathQueryResponse.setType(ConstCorrespond.TRAN_TYPE[pathInfoDO.getMainType()]);
                pathQueryResponse.setStart(pathInfoDO.getStart());
                pathQueryResponse.setEnd(pathInfoDO.getEnd());
                pathQueryResponse.setRisk((int) (assessDO.getSumScore() * 10 + 0.5) / 10.0);
                return pathQueryResponse;
            }).collect(Collectors.toList()));

            pathQueryListResponse.setNum(assessDOMapper.count(TimeTool.timeToDaySy(TimeTool.todayCreate().getTime()), fixTool.provinceUni(pathQueryRequest.getProvince())));
            return ResultTool.success(pathQueryListResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(500, e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 单地址返校信息
     * @Param: [startAddress]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/12
     */
    @Override
    public Result pathChildQuery(String startAddress) {
        try {
            //查询path表
            PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
            pathInfoDOExample.createCriteria().andStartEqualTo(startAddress);
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);
            if (pathInfoDOList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "查无此地的路径方案！");
            }

            List<Integer> pathIdList = pathInfoDOList.stream().map(PathInfoDO::getId).collect(Collectors.toList());

            //查询pass表
            PassInfoDOExample passInfoDOExample = new PassInfoDOExample();
            passInfoDOExample.createCriteria().andPathIdIn(pathIdList);
            passInfoDOExample.setOrderByClause("order_id ASC, id ASC");
            List<PassInfoDO> passInfoDOList = passInfoDOMapper.selectByExample(passInfoDOExample);

            //查询Assess表
            AssessDOExample assessDOExample = new AssessDOExample();
            assessDOExample.createCriteria()
                    .andPathIdIn(pathIdList)
                    .andUpdateTimeEqualTo(TimeTool.todayCreate().getTime());
            assessDOExample.setOrderByClause("pass_order ASC");
            List<AssessDO> assessDOList = assessDOMapper.selectByExample(assessDOExample);

            //查询impArea表
            ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
            impAreaDOExample.createCriteria()
                    .andDateEqualTo(TimeTool.todayCreate().getTime());
            List<ImpAreaDO> impAreaDOList = impAreaDOMapper.selectByExample(impAreaDOExample);
            Map<String, ImpAreaDO> impAreaDOMap = impAreaDOList.stream().collect(Collectors.toMap(ImpAreaDO::getName, impAreaDO -> impAreaDO));

            //初始化对象
            PathChildResponse pathChildResponse = new PathChildResponse();

            for (PathInfoDO pathInfoDO : pathInfoDOList) {
                PathResponse pathResponse = new PathResponse();

                //获取passInfo表信息
                List<PassInfoDO> passInfoDOS = passInfoDOList.stream().filter(passInfoDO -> passInfoDO.getPathId().equals(pathInfoDO.getId()))
                        .collect(Collectors.toList());
                if (passInfoDOS.isEmpty()) {
                    throw new AllException(EmAllException.DATABASE_ERROR, "无路段信息");
                }

                //获取分数信息
                List<AssessDO> assessDOS = assessDOList.stream().filter(assessDO -> assessDO.getPathId().equals(pathInfoDO.getId()))
                        .collect(Collectors.toList());
                if (assessDOS.isEmpty()) {
                    throw new AllException(EmAllException.DATABASE_ERROR, "无风险信息");
                }

                //初始化类
                pathResponse.setType(ConstCorrespond.TRAN_TYPE[pathInfoDO.getMainType()]);
                pathResponse.setSumScore(NumberTool.doubleToStringWotH(assessDOS.get(0).getSumScore()));
                pathResponse.setResultList(new ArrayList<>());
                pathResponse.setTime(TimeTool.timeSlotToString(assessDOS.get(0).getSumTime()));

                int i = 0;
                for (AssessDO assessDO : assessDOS) {
                    if (i >= passInfoDOS.size() || assessDO.getPassOrder() < passInfoDOS.get(i).getOrderId()) continue;
                    RouteCalReponse routeCalReponse = new RouteCalReponse();
                    routeCalReponse.setTimeScore(NumberTool.doubleToStringWotH(assessDO.getTimeScore()));
                    routeCalReponse.setFinalscore((int) (assessDO.getFinalScore() * 10 + 0.5) / 10.0);

                    double temp = ConstCorrespond.ROUTE_WEIGHT[0] / (ConstCorrespond.ROUTE_WEIGHT[0] + ConstCorrespond.ROUTE_WEIGHT[2]);
                    routeCalReponse.setTransportScore(String.valueOf(temp * assessDO.getCleanlinessScore() + (1 - temp) * assessDO.getCrowdScore()));
                    routeCalReponse.setTime(TimeTool.timeSlotToString(assessDO.getTime()));
                    routeCalReponse.setCity(new ArrayList<>());

                    boolean flag = false;
                    while (i < passInfoDOS.size()
                            && assessDO.getPassOrder().equals(passInfoDOS.get(i).getOrderId())) {
                        PassInfoDO tempPassInfo = passInfoDOS.get(i++);
                        if (!flag) {
                            routeCalReponse.setTitle(tempPassInfo.getTitle());
                            routeCalReponse.setStart(tempPassInfo.getStartAddress());
                            routeCalReponse.setEnd(tempPassInfo.getEndAddress());
                            flag = true;
                        }

                        CityCal cityCal = new CityCal();
                        cityCal.setCityname(tempPassInfo.getArea());
                        ImpAreaDO impAreaDO = impAreaDOMap.get(fixTool.areaUni(tempPassInfo.getArea()));
                        if (impAreaDO == null) {
                            impAreaDO = impAreaDOMap.get(tempPassInfo.getArea());
                            if (impAreaDO == null) {
                                for (ImpAreaDO tempImpAreaDo : impAreaDOMap.values()) {
                                    if (tempImpAreaDo.getName().contains(fixTool.areaUni(tempPassInfo.getArea()))) {
                                        impAreaDO = tempImpAreaDo;
                                        break;
                                    }
                                }
                                if (impAreaDO == null) {
                                    continue;
                                }
                            }
                        }
                        cityCal.setCityscore(impAreaDO.getSumScore().intValue());
                        routeCalReponse.getCity().add(cityCal);
                    }
                    pathResponse.getResultList().add(routeCalReponse);
                }
                pathChildResponse.getTransit().add(pathResponse);
            }
            return ResultTool.success(pathChildResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(500, e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 热力图绘制
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/12
     */
    @Override
    public Result pathMap() {
        //查询ImpArea表
        ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
        impAreaDOExample.createCriteria()
                .andDateEqualTo(TimeTool.todayCreate().getTime());
        List<ImpAreaDO> impAreaDOList = impAreaDOMapper.selectByExample(impAreaDOExample);

        //查询area表;
        Map<String, AreaDO> cityMap = areaDOMapper.getCityMap();

        List<PathMapResponse> pathMapResponseList = new ArrayList<>();
        for (ImpAreaDO impAreaDO : impAreaDOList) {
            PathMapResponse pathMapResponse = new PathMapResponse();
            pathMapResponse.setCount(impAreaDO.getSumScore());
            AreaDO areaDO = cityMap.get(fixTool.areaUni(impAreaDO.getName()));
            if (areaDO == null) {
                areaDO = cityMap.get(impAreaDO.getName());
                if (areaDO == null) {
                    for (AreaDO areaDOTemp : cityMap.values()) {
                        if (areaDOTemp.getName().contains(fixTool.areaUni(impAreaDO.getName()))) {
                            areaDO = areaDOTemp;
                        }
                    }
                    if (areaDO == null) continue;
                }
            }

            pathMapResponse.setLat(areaDO.getLat());
            pathMapResponse.setLng(areaDO.getLng());
            pathMapResponseList.add(pathMapResponse);
        }
        return ResultTool.success(pathMapResponseList);
    }

    /**
     * @Description: 将百度地图api的查询结果存入pathInfo,和passInfo中
     * @Param: [startaddress]
     * @return: com.nCov.DataView.model.response.Result
     * @Author: pongshy
     * @Date: 2020/4/12
     */
    @Async
    @Override
    public void writeInPathAndPass(Integer id) throws AllException, IOException, ParseException {
        final String endAddress = "上海大学宝山校区";

        List<StudentInformationDO> studentInformationDOList = studentInformationDOMapper.selectStartFrom(id);

        if (studentInformationDOList.size() == 0) {
            throw new AllException(EmAllException.DATABASE_ERROR, "数据库中暂无学生地址信息");
        }
        for (StudentInformationDO studentInformationDO : studentInformationDOList) {
            String startAddress = "";
            if (studentInformationDO.getCountry().equals("中国")) {
                startAddress = studentInformationDO.getCountry() +
                        studentInformationDO.getProvince() +
                        studentInformationDO.getCity() +
                        studentInformationDO.getArea() +
                        studentInformationDO.getAddress();
                //调用百度地图api查询路径信息
                try {
                    PathInfoDOExample pathInfoDOExample1 = new PathInfoDOExample();
                    pathInfoDOExample1.createCriteria().andStartEqualTo(startAddress);
                    if (pathInfoDOMapper.countByExample(pathInfoDOExample1) > 0) {
                        pathInfoDOExample1.clear();
                        continue;
                    }
                    pathInfoDOExample1.clear();

                    List<RouteInfo> routeInfoList = baiduTool.pathInfo(startAddress, endAddress);
                    //n条路线
                    for (RouteInfo routeInfo : routeInfoList) {
                        //每一条线路的详细信息
                        List<RouteCalRequest> routeCalRequestList = routeInfo.getRouteCalRequestList();
                        if (routeCalRequestList.isEmpty()) {
                            throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "百度api获取的路径信息为空");
                        }
                        PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
                        //每一条路径信息，存入数据库
                        int routeNum = 0;
                            //每一条路线
                        int pathId = 0;
                        PathInfoDO record = new PathInfoDO();

                        record.setStart(startAddress);
                        record.setEnd(endAddress);
                        record.setMainType(routeNum);
                        record.setSumTime(routeInfo.getSumTime());
                        if (pathInfoDOMapper.insertSelective(record) != 1) {
                            throw new AllException(EmAllException.DATABASE_ERROR, "无法插入pathInfo中");
                        }

                        pathInfoDOExample.createCriteria().andStartEqualTo(startAddress).andMainTypeEqualTo(routeNum);
                        PathInfoDO pathInfoDO = pathInfoDOMapper.selectByExample(pathInfoDOExample).get(0);
                        pathId = pathInfoDO.getId();
                        pathInfoDOExample.clear();

                        int order_num = 0;
                        int main_type = 0;
                        //插入PassInfo这张表中
                        for (RouteCalRequest routeCalRequest : routeCalRequestList) {
                            List<PassInfoDO> passInfoDOList = new ArrayList<>();

                            for (String city : routeCalRequest.getCitys()) {
                                PassInfoDO passInfoDO_record = new PassInfoDO();
                                passInfoDO_record.setArea(city);
                                passInfoDO_record.setDistance((int) routeCalRequest.getDistance());
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
                        //更新PathInfo中刚插入的那条数据的main_type
                        pathInfoDO.setMainType(main_type);
                        pathInfoDOMapper.updateByPrimaryKeySelective(pathInfoDO);
                    }
                }
                catch (AllException e) {
                    log.info(e.getMsg());
                    log.info(startAddress + " 无法存入数据库");
                    if (e.getMsg().equals("配额已到上限")) {
                        log.info("因配额达到上限，读入结束");
                        return;
                    }
                }
                catch (IOException e) {
                    log.info(e.getMessage());
                    log.info(startAddress + " 无法存入数据库");
                }
                catch (Exception e) {
                    log.info(e.getMessage());
                    log.info(startAddress + " 无法存入数据库");
                }
            }
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
                covRankResponse.setWeekGrowth(NumberTool.doubleToStringWithH(impAreaDO.getWeekGrowth()));
                covRankResponse.setWeekGrowthScore(impAreaDO.getWeekScore());
                return covRankResponse;
            }).collect(Collectors.toList());
        }

        if (!date.matches("^[0-9]{4}-[0-9]+-[0-9]+")) {
            throw new AllException(EmAllException.BAD_REQUEST, "日期格式出错!");
        }
        Map<String, AbroadInputDO> abroadInputMap = abroadInputDOMapper.getInfoByDate(date);
        if (abroadInputMap.isEmpty()) {
            throw new AllException(EmAllException.DATABASE_ERROR, date + "的境外输入数据不存在");
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

        calendar.add(Calendar.DATE, -7);
        //防止日期在1月24日前
        while (calendar.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
            calendar.add(Calendar.DATE, 1);
        }

        calendarTemp.setTime(calendar.getTime());
        Map<String, CovData> covDataMapWeek = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendar.getTime()));
        //找到有数据的那天，添加
        if (covDataMapWeek.isEmpty()) {
            while (covDataMapWeek.isEmpty() && !calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                calendarTemp.add(Calendar.DATE, -1);
                covDataMapWeek = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendarTemp.getTime()));
            }
            if (calendarTemp.getTime().before(TimeTool.stringToDay("2020-01-24"))) {
                throw new AllException(EmAllException.DATABASE_ERROR, "暂无疫情数据");
            }
            for (CovData covData : covDataMapWeek.values()) {
                covData.setId(null);
                covData.setDate(calendar.getTime());
            }
            covDataMapper.insertList(new ArrayList<>(covDataMapWeek.values()));
        }

        calendar.add(Calendar.DATE, -6);
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
            CovData covDataWeek;

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

            if ((covDataWeek = covDataMapWeek.get(areaDO.getName())) == null) {
                List<CovData> listNow = covDataMapWeek.values()
                        .stream()
                        .filter(covData -> covData.getAreaname().contains(areaDO.getName()))
                        .collect(Collectors.toList());
                if (listNow.isEmpty()) {
                    if ((covDataWeek = fixTool.fixCovDate(TimeTool.timeToDaySy(calendar.getTime()), areaDO.getName())) == null) {
                        continue;
                    }
                } else {
                    covDataWeek = listNow.get(0);
                }
            }

            double RemainConfirm = Double.parseDouble(NumberTool.doubleToStringWotH(NumberTool.intDivision(covDataNow.getTotalconfirm() - covDataNow.getTotaldead() - covDataNow.getTotalheal(), areaDO.getPopulation()) * 1000000));
            impAreaDO.setRemainConfirm(Math.max(RemainConfirm, 0));
            int count = covDataNow.getTotalconfirm() - covDataNow.getTotalheal() - covDataNow.getTotaldead();
            impAreaDO.setRemainCount(Math.max(count, 0));
            int growth = covDataNow.getTotalconfirm() - covDataThr.getTotalconfirm();
            impAreaDO.setGrowth(Math.max(growth, 0));
            double weekGrowth = NumberTool.intDivision(covDataNow.getTotalconfirm(), covDataWeek.getTotalconfirm());
            impAreaDO.setWeekGrowth(Math.max(weekGrowth, 1));

            AbroadInputDO abroadInputDO = abroadInputMap.get(fixTool.provinceUni(impAreaDO.getProvinceName()));
            if (abroadInputDO == null) {
                log.info(impAreaDO.getProvinceName() + "名称可能无法标准化.");
                for (AbroadInputDO abroadInputDOTRA : abroadInputMap.values()) {
                    if (impAreaDO.getProvinceName().contains(fixTool.provinceUni(abroadInputDOTRA.getProvincename()))) {
                        abroadInputDO = abroadInputDOTRA;
                    }
                }
                if (abroadInputDO == null) {
                    synchronized (AbroadLock) {
                        AbroadInputDOExample abroadInputDOExample = new AbroadInputDOExample();
                        abroadInputDOExample.createCriteria().andDateEqualTo(TimeTool.stringToDay(date))
                                .andProvincenameLike(fixTool.provinceUni(impAreaDO.getProvinceName()) + "%");
                        List<AbroadInputDO> abroadInputDOList = abroadInputDOMapper.selectByExample(abroadInputDOExample);
                        if (!abroadInputDOList.isEmpty()) abroadInputDO = abroadInputDOList.get(0);
                        else {
                            abroadInputDO = new AbroadInputDO();
                            abroadInputDO.setDate(TimeTool.stringToDay(date));
                            abroadInputDO.setThenumber(0);
                            abroadInputDO.setProvincename(fixTool.provinceUni(impAreaDO.getProvinceName()));
                            abroadInputDOMapper.insertSelective(abroadInputDO);
                        }
                    }
                }
            }

            impAreaDO.setAbroadInput(abroadInputDO.getThenumber());
            impAreaDOList.add(impAreaDO);
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getAbroadInput).reversed());
        int number = 1;
        double last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getAbroadInput();
                impAreaDOList.get(0).setAbroadInputRank(number++);
            } else if (last == impAreaDOList.get(i).getAbroadInput()) {
                impAreaDOList.get(i).setAbroadInputRank(impAreaDOList.get(i - 1).getAbroadInputRank());
                number++;
            } else {
                last = impAreaDOList.get(i).getAbroadInput();
                impAreaDOList.get(i).setAbroadInputRank(number++);
            }
        }
        int lastRank = impAreaDOList.get(impAreaDOList.size() - 1).getAbroadInputRank();
        for (ImpAreaDO covRank : impAreaDOList) {
            covRank.setAbroadInputScore(NumberTool.Score(covRank.getAbroadInputRank(), lastRank));
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getWeekGrowth).reversed());
        number = 1;
        last = 0;
        for (int i = 0; i < impAreaDOList.size(); i++) {
            if (i == 0) {
                last = impAreaDOList.get(0).getWeekGrowth();
                impAreaDOList.get(0).setWeekGrowthRank(number++);
            } else if (last == impAreaDOList.get(i).getWeekGrowth()) {
                impAreaDOList.get(i).setWeekGrowthRank(impAreaDOList.get(i - 1).getWeekGrowthRank());
                number++;
            } else {
                last = impAreaDOList.get(i).getWeekGrowth();
                impAreaDOList.get(i).setWeekGrowthRank(number++);
            }
        }
        lastRank = impAreaDOList.get(impAreaDOList.size() - 1).getWeekGrowthRank();
        for (ImpAreaDO covRank : impAreaDOList) {
            covRank.setWeekScore(NumberTool.Score(covRank.getWeekGrowthRank(), lastRank));
        }

        impAreaDOList.sort(Comparator.comparing(ImpAreaDO::getRemainConfirm).reversed());
        number = 1;
        last = 0;
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
        lastRank = impAreaDOList.get(impAreaDOList.size() - 1).getRemainConfirmRank();
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
            impAreaDO.setSumScore(impAreaDO.getRemainCountScore() * ConstCorrespond.CITY_WEIGHT[0]
                    + impAreaDO.getRemainScore() * ConstCorrespond.CITY_WEIGHT[1]
                    + impAreaDO.getGrowthScore() * ConstCorrespond.CITY_WEIGHT[2]
                    + impAreaDO.getWeekScore() * ConstCorrespond.CITY_WEIGHT[3]
                    + impAreaDO.getAbroadInput() * ConstCorrespond.CITY_WEIGHT[4]
            );
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

        synchronized (lock) {
            List<ImpAreaDO> impAreaListTemp = impAreaDOMapper.selectByExample(impAreaDOExample);
            if (!impAreaListTemp.isEmpty()) {
                return this.allAreaCal(date);
            }
            impAreaDOMapper.insertList(impAreaDOList);
        }

        return impAreaDOList.stream().map(impAreaDO -> {
            CovRankResponse covRankResponse = new CovRankResponse();
            BeanUtils.copyProperties(impAreaDO, covRankResponse);
            covRankResponse.setProvincename(impAreaDO.getProvinceName());
            covRankResponse.setAllRank(impAreaDO.getAllrank());
            covRankResponse.setWeekGrowth(NumberTool.doubleToStringWithH(impAreaDO.getWeekGrowth()));
            covRankResponse.setWeekGrowthScore(impAreaDO.getWeekScore());
            return covRankResponse;
        }).collect(Collectors.toList());
    }


    /**
     * @Description: 每天对各路段进行评估
     * @Param: []
     * @return: void
     * @Author: SoCMo
     * @Date: 2020/4/10
     */
    @Scheduled(cron = "0 2 0 * * *")
    public void assess() {
        try {
            //获取path表信息
            PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);
            if (pathInfoDOList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "pathInfo表为空");
            }

            //获取impArea的信息
            ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
            Calendar calendar = TimeTool.todayCreate();
            List<CovRankResponse> covDataList = this.allAreaCal(TimeTool.timeToDaySy(calendar.getTime()));
            Map<String, CovRankResponse> covRankResponseMap = covDataList.stream().collect(Collectors.toMap(CovRankResponse::getName, covRankResponse -> covRankResponse));

            //查询Area表的信息
            Map<Integer, AreaDO> provinceMapInt = areaDOMapper.getProvinceMapInt();
            Map<String, AreaDO> cityMap = areaDOMapper.getCityMap();

            //计算
            for (PathInfoDO pathInfoDO : pathInfoDOList) {
                PassInfoDOExample passInfoDOExample = new PassInfoDOExample();
                passInfoDOExample.createCriteria().andPathIdEqualTo(pathInfoDO.getId());
                List<PassInfoDO> passInfoDOS = passInfoDOMapper.selectByExample(passInfoDOExample);
                if (passInfoDOS.isEmpty()) {
                    log.error("id为:" + pathInfoDO.getId() + "的路径无路段信息");
                    continue;
                }

                int order = 0;
                List<AssessDO> sumList = new ArrayList<>();
                List<AssessDO> finalList = new ArrayList<>();
                AreaDO city = cityMap.get(fixTool.areaUni(passInfoDOS.get(0).getArea()));
                if (city == null) {
                    city = cityMap.get(passInfoDOS.get(0).getArea());
                    if (city == null) {
                        for (AreaDO areaDOTemp : cityMap.values()) {
                            if (areaDOTemp.getName().contains(fixTool.areaUni(passInfoDOS.get(0).getArea()))) {
                                city = areaDOTemp;
                            }
                        }
                        if (city == null) {
                            log.error(passInfoDOS.get(0).getArea() + "在area表中不存在!");
                            continue;
                        }
                    }
                }

                String province = "";
                List<String> provinceList = provinceMapInt.values().stream().map(AreaDO::getName).collect(Collectors.toList());
                Pattern pattern = Pattern.compile("^中国(.{2})");
                Matcher matcher = pattern.matcher(pathInfoDO.getStart());
                if (!matcher.find()) continue;

                if (provinceList.contains(matcher.group(1))) {
                    province = matcher.group(1);
                    if (province.equals("上海")) {
                        String area = pathInfoDO.getStart();
                        Pattern patternTemp = Pattern.compile("上海市(.+?)区");
                        Matcher matcherTemp = patternTemp.matcher(area);

                        if (!matcherTemp.find()) continue;
                        province = matcherTemp.group(0);
                    }
                } else {
                    String area = pathInfoDO.getStart();
                    Pattern patternTemp = Pattern.compile("^中国(.{3})");
                    Matcher matcherTemp = patternTemp.matcher(area);

                    if (!matcherTemp.find()) continue;
                    province = matcherTemp.group(1);
                    if (!provinceList.contains(province)) continue;
                }

                for (PassInfoDO passInfoDO : passInfoDOS) {
                    if (order < passInfoDO.getOrderId() && !finalList.isEmpty()) {
                        double localScore = finalList.stream().mapToDouble(AssessDO::getLocalScore).max().getAsDouble();
                        double finalScore = ConstCorrespond.ROUTE_WEIGHT[0] * finalList.get(0).getCrowdScore()
                                + ConstCorrespond.ROUTE_WEIGHT[1] * finalList.get(0).getTimeScore()
                                + ConstCorrespond.ROUTE_WEIGHT[2] * finalList.get(0).getCleanlinessScore()
                                + ConstCorrespond.ROUTE_WEIGHT[3] * localScore;
                        for (AssessDO assessDO : finalList) {
                            assessDO.setFinalScore(finalScore);
                            sumList.add(assessDO);
                        }
                        order++;
                        finalList.clear();
                    }
                    AssessDO assessDO = new AssessDO();
                    assessDO.setPathId(pathInfoDO.getId());
                    assessDO.setPassOrder(passInfoDO.getOrderId());
                    assessDO.setStartAddress(pathInfoDO.getStart());

                    assessDO.setAreaName(province);
                    assessDO.setSumTime(pathInfoDO.getSumTime());
                    assessDO.setUpdateTime(TimeTool.todayCreate().getTime());

                    assessDO.setCleanlinessScore((int) ConstCorrespond.CLEAN_SCORE[passInfoDO.getTypeNum()]);
                    assessDO.setCrowdScore((int) ConstCorrespond.CROWD[passInfoDO.getTypeNum()]);
                    assessDO.setTime(passInfoDO.getDistance() / ConstCorrespond.SPEED[passInfoDO.getTypeNum()] / 1000);
                    assessDO.setTimeScore(assessDO.getTime() >= 6 ? 100 : (assessDO.getTime() / 6) * 100);

                    CovRankResponse covRankResponse = covRankResponseMap.get(fixTool.areaUni(passInfoDO.getArea()));
                    if (covRankResponse == null) {
                        covRankResponse = covRankResponseMap.get(passInfoDO.getArea());
                        if (covRankResponse == null) {
                            for (CovRankResponse covRankResponseTemp : covRankResponseMap.values()) {
                                if (covRankResponseTemp.getName().contains(fixTool.areaUni(passInfoDO.getArea()))) {
                                    covRankResponse = covRankResponseTemp;
                                    break;
                                }
                            }
                            if (covRankResponse == null) {
                                if (passInfoDO.getArea().equals("浦东新区")) {
                                    covRankResponse = covRankResponseMap.get(passInfoDO.getArea());
                                }
                                if (covRankResponse == null) {
                                    continue;
                                }
                            }
                        }
                    }
                    assessDO.setLocalScore((int) covRankResponse.getSumScore());
                    finalList.add(assessDO);
                }
                if (!finalList.isEmpty()) {
                    double localScore = finalList.stream().mapToDouble(AssessDO::getLocalScore).max().getAsDouble();
                    double finalScore = ConstCorrespond.ROUTE_WEIGHT[0] * finalList.get(0).getCrowdScore()
                            + ConstCorrespond.ROUTE_WEIGHT[1] * finalList.get(0).getTimeScore()
                            + ConstCorrespond.ROUTE_WEIGHT[2] * finalList.get(0).getCleanlinessScore()
                            + ConstCorrespond.ROUTE_WEIGHT[3] * localScore;
                    for (AssessDO assessDO : finalList) {
                        assessDO.setFinalScore(finalScore);
                        sumList.add(assessDO);
                    }
                    finalList.clear();
                }

                double maxScore = 0;
                for (AssessDO assessDO : sumList) {
                    if (assessDO.getFinalScore() > maxScore) maxScore = assessDO.getFinalScore();
                }
                for (AssessDO assessDO : sumList) {
                    assessDO.setSumScore(maxScore);
                }

                assessDOMapper.insertList(sumList);
            }
        } catch (AllException e) {
            log.error(e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
