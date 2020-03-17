package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.AreaDOMapper;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.entity.AreaDO;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import com.nCov.DataView.model.entity.CovRank;
import com.nCov.DataView.model.request.AllAreaRequest;
import com.nCov.DataView.model.request.AreaInfoRequest;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.AreaInfoResponse;
import com.nCov.DataView.model.response.info.CovRankResponse;
import com.nCov.DataView.model.response.info.DateInfoResponse;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.FixTool;
import com.nCov.DataView.tools.NumberTool;
import com.nCov.DataView.tools.ResultTool;
import com.nCov.DataView.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    private FixTool fixTool;

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
            Map<String, AreaDO> cityMap = areaDOMapper.getCityMap();
            if (cityMap.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "地区数据为空");
            }
            Map<Integer, AreaDO> provinceMap = areaDOMapper.getProvinceMapInt();

            //获取三天前的时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TimeTool.stringToDay(date));
            Map<String, CovData> covDataListNow = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendar.getTime()));
            if (covDataListNow.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "三天前的数据为空。");
            }

            calendar.add(Calendar.DATE, -3);
            Map<String, CovData> covDataListThr = covDataMapper.getInfoByDate(TimeTool.timeToDaySy(calendar.getTime()));
            if (covDataListThr.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "三天前的数据为空。");
            }

            List<CovRank> covRankList = new ArrayList<>();
            for (AreaDO areaDO : cityMap.values()) {
                CovRank covRank = new CovRank();
                BeanUtils.copyProperties(areaDO, covRank);
                covRank.setProvincename(provinceMap.get(areaDO.getParentid()).getName());

                CovData covDataNow;
                CovData covDataThr;
                if ((covDataNow = covDataListNow.get(areaDO.getName())) == null) {
                    List<CovData> listNow = covDataListNow.values()
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

                if ((covDataThr = covDataListThr.get(areaDO.getName())) == null) {
                    List<CovData> listThr = covDataListThr.values()
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
                covRank.setRemainConfirm(NumberTool.intDivision(covDataNow.getTotalconfirm() - covDataNow.getTotaldead() - covDataNow.getTotalheal(), areaDO.getPopulation()) * 1000000);
                covRank.setDead(NumberTool.intDivision(covDataNow.getTotaldead(), covDataNow.getTotalconfirm()));
                covRank.setGrowth(NumberTool.intDivision(covDataNow.getTotalconfirm(), covDataThr.getTotalconfirm()));
                covRankList.add(covRank);
            }

            covRankList.sort(Comparator.comparing(CovRank::getRemainConfirm).reversed());
            int number = 1;
            double last = 0;
            for (int i = 0; i < covRankList.size(); i++) {
                if (i == 0) {
                    last = covRankList.get(0).getRemainConfirm();
                    covRankList.get(0).setRemainConfirmRank(number++);
                } else if (last == covRankList.get(i).getRemainConfirm()) {
                    covRankList.get(i).setRemainConfirmRank(covRankList.get(i - 1).getRemainConfirmRank());
                    number++;
                } else {
                    last = covRankList.get(i).getRemainConfirm();
                    covRankList.get(i).setRemainConfirmRank(number++);
                }
            }

            covRankList.sort(Comparator.comparing(CovRank::getDead).reversed());
            number = 1;
            last = 0;
            for (int i = 0; i < covRankList.size(); i++) {
                if (i == 0) {
                    last = covRankList.get(0).getDead();
                    covRankList.get(0).setDeadRank(number++);
                } else if (last == covRankList.get(i).getDead()) {
                    covRankList.get(i).setDeadRank(covRankList.get(i - 1).getDeadRank());
                    number++;
                } else {
                    last = covRankList.get(i).getDead();
                    covRankList.get(i).setDeadRank(number++);
                }
            }

            covRankList.sort(Comparator.comparing(CovRank::getGrowth).reversed());
            number = 1;
            last = 0;
            for (int i = 0; i < covRankList.size(); i++) {
                if (i == 0) {
                    last = covRankList.get(0).getGrowth();
                    covRankList.get(0).setGrowthRank(number++);
                } else if (last == covRankList.get(i).getGrowth()) {
                    covRankList.get(i).setGrowthRank(covRankList.get(i - 1).getGrowthRank());
                    number++;
                } else {
                    last = covRankList.get(i).getGrowth();
                    covRankList.get(i).setGrowthRank(number++);
                }
            }


            for (CovRank covRank : covRankList) {
                covRank.setRemainScore(NumberTool.Score(covRank.getRemainConfirmRank(), covRankList.size()));
                covRank.setDeadScore(NumberTool.Score(covRank.getDeadRank(), covRankList.size()));
                covRank.setGrowthScore(NumberTool.Score(covRank.getGrowthRank(), covRankList.size()));
                covRank.setSumScore(covRank.getDeadScore() * 0.2 + covRank.getRemainScore() * 0.5 + covRank.getGrowthScore() * 0.3);
            }

            covRankList.sort(Comparator.comparing(CovRank::getSumScore).reversed());
            number = 1;
            last = 0;
            for (int i = 0; i < covRankList.size(); i++) {
                if (i == 0) {
                    last = covRankList.get(0).getSumScore();
                    covRankList.get(0).setAllRank(number++);
                } else if (last == covRankList.get(i).getSumScore()) {
                    covRankList.get(i).setAllRank(covRankList.get(i - 1).getAllRank());
                    number++;
                } else {
                    last = covRankList.get(i).getSumScore();
                    covRankList.get(i).setAllRank(number++);
                }
            }

            return ResultTool.success(covRankList.stream().map(covRank -> {
                CovRankResponse covRankResponse = new CovRankResponse();
                BeanUtils.copyProperties(covRank, covRankResponse);
                covRankResponse.setGrowth(NumberTool.doubleToString(covRank.getGrowth()));
                covRankResponse.setDead(NumberTool.doubleToString(covRank.getDead()));
                covRankResponse.setSumScore(String.format("%.2f", covRank.getSumScore()));
                return covRankResponse;
            }).collect(Collectors.toList()));
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }
}
