package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.AbroadInputDOMapper;
import com.nCov.DataView.dao.AreaDOMapper;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.dao.StatisticDOMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.entity.*;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.AreaInfo;
import com.nCov.DataView.model.response.info.DayInfoResponse;
import com.nCov.DataView.model.response.info.StatisticResponse;
import com.nCov.DataView.service.MapService;
import com.nCov.DataView.tools.FixTool;
import com.nCov.DataView.tools.NumberTool;
import com.nCov.DataView.tools.ResultTool;
import com.nCov.DataView.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MapServiceImpl implements MapService {
    @Resource
    private AreaDOMapper areaDOMapper;

    @Resource
    private CovDataMapper covDataMapper;

    @Resource
    private AbroadInputDOMapper abroadInputDOMapper;

    @Resource
    private StatisticDOMapper statisticDOMapper;

    @Resource
    private FixTool fixTool;

    /**
     * @Description: 每日各个省疫情数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/10
     */
    @Override
    public Result dateInfo(String date) {
        try {
            CovDataExample covDataExample = new CovDataExample();
            covDataExample.createCriteria()
                    .andIsprovinceEqualTo(1)
                    .andDateEqualTo(TimeTool.stringToDay(date))
                    .andDateLessThan(TimeTool.stringToDay("2020-04-04"));
            List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
            if (covDataList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, date + "数据为空！");
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TimeTool.stringToDay(date));
            calendar.add(Calendar.DATE, -7);

            Calendar calendarTemp = (Calendar) calendar.clone();
            covDataExample.clear();
            covDataExample.createCriteria()
                    .andIsprovinceEqualTo(1)
                    .andDateEqualTo(calendar.getTime());
            List<CovData> CovDataListSeven = covDataMapper.selectByExample(covDataExample);
            while (CovDataListSeven.isEmpty() && calendarTemp.before(TimeTool.stringToDay(date))) {
                calendarTemp.add(Calendar.DATE, 1);
                covDataExample.clear();
                covDataExample.createCriteria()
                        .andIsprovinceEqualTo(1)
                        .andDateEqualTo(calendar.getTime());
                CovDataListSeven = covDataMapper.selectByExample(covDataExample);
            }

            calendarTemp.clear();
            calendar.add(Calendar.DATE, -7);
            calendarTemp = (Calendar) calendar.clone();
            covDataExample.clear();
            covDataExample.createCriteria()
                    .andIsprovinceEqualTo(1)
                    .andDateEqualTo(calendar.getTime());
            List<CovData> CovDataListFour = covDataMapper.selectByExample(covDataExample);
            while (CovDataListFour.isEmpty() && calendarTemp.before(TimeTool.stringToDay(date))) {
                calendarTemp.add(Calendar.DATE, 1);
                covDataExample.clear();
                covDataExample.createCriteria()
                        .andIsprovinceEqualTo(1)
                        .andDateEqualTo(calendar.getTime());
                CovDataListFour = covDataMapper.selectByExample(covDataExample);
            }

            Map<String, AreaDO> areaDOMap = areaDOMapper.getProvinceMapString();

            AbroadInputDOExample abroadInputDOExample = new AbroadInputDOExample();
            abroadInputDOExample.createCriteria().andDateEqualTo(TimeTool.stringToDay(date));
            List<AbroadInputDO> abroadInputDOList = abroadInputDOMapper.selectByExample(abroadInputDOExample);
            abroadInputDOExample.clear();

            calendar.setTime(TimeTool.stringToDay(date));
            calendar.add(Calendar.DATE, -1);
            abroadInputDOExample.createCriteria().andDateEqualTo(calendar.getTime());
            List<AbroadInputDO> abroadInputDOListYD = abroadInputDOMapper.selectByExample(abroadInputDOExample);

            //初始化当天返回体
            DayInfoResponse dayInfoResponse = new DayInfoResponse();
            dayInfoResponse.setDate(date);
            dayInfoResponse.setProvinceInfoList(new ArrayList<>());

            for (AreaDO areaDO : areaDOMap.values()) {
                AreaInfo areaInfo = new AreaInfo();
                CovData covData = null;
                for (CovData tempCovData : covDataList) {
                    if (tempCovData.getAreaname().contains(areaDO.getName())) {
                        covData = tempCovData;
                    }
                }
                if (covData == null) {
                    covData = fixTool.fixCovDate(TimeTool.timeToDaySy(calendar.getTime()), areaDO.getName());
                    if (covData == null) {
                        throw new AllException(EmAllException.DATABASE_ERROR, areaDO.getName() + "数据为空");
                    }
                }

                areaInfo.setName(areaDO.getName());
                areaInfo.setConfirm(covData.getTotalconfirm());
                double remainInMillion = NumberTool.intDivision(covData.getTotalconfirm(), areaDO.getPopulation()) * 1000000;
                remainInMillion = (int) (remainInMillion * 100 + 0.5) / 100.0;
                areaInfo.setRemainInMillion(remainInMillion);
                areaInfo.setDead(covData.getTotaldead());
                areaInfo.setHeal(covData.getTotalheal());
                int remain = covData.getTotalconfirm() - covData.getTotalheal() - covData.getTotaldead();
                areaInfo.setRemainConfirm(Math.max(remain, 0));

                if (CovDataListSeven.isEmpty()) {
                    areaInfo.setWeekGrowth(0);
                } else {
                    CovData covDataSeven = null;
                    for (CovData covDataTemp : CovDataListFour) {
                        if (covDataTemp.getProvincename().contains(fixTool.provinceUni(areaDO.getName()))) {
                            covDataSeven = covDataTemp;
                        }
                    }
                    if (covDataSeven == null) {
                        covDataSeven = fixTool.fixCovDate(TimeTool.timeToDaySy(calendar.getTime()), areaDO.getName());
                        if (covDataSeven == null) {
                            areaInfo.setWeekGrowth(1.0);
                        }
                    }

                    if (covDataSeven != null) {
                        double weekGrowth = covDataSeven.getTotalconfirm() == 0 ? 1.0 : NumberTool.intDivision(covData.getTotalconfirm(), covDataSeven.getTotalconfirm());
                        weekGrowth = (int) (weekGrowth * 100 + 0.5) / 100.0;
                        areaInfo.setWeekGrowth(weekGrowth);
                    }
                }

                if (CovDataListFour.isEmpty()) {
                    areaInfo.setWeekGrowth(0);
                } else {
                    CovData covDataFour = null;
                    for (CovData covDataTemp : CovDataListFour) {
                        if (covDataTemp.getProvincename().contains(fixTool.provinceUni(areaDO.getName()))) {
                            covDataFour = covDataTemp;
                        }
                    }
                    if (covDataFour == null) {
                        covDataFour = fixTool.fixCovDate(TimeTool.timeToDaySy(calendar.getTime()), areaDO.getName());
                    }
                    areaInfo.setGrowth(Math.max(covData.getTotalconfirm() - covDataFour.getTotalconfirm(), 0));
                }

                if (abroadInputDOList.isEmpty() || abroadInputDOListYD.isEmpty()) {
                    areaInfo.setAbroadInput(0);
                } else {
                    AbroadInputDO abroadInputDO = null;
                    AbroadInputDO abroadInputDOYD = null;
                    for (AbroadInputDO abroadInputDOTemp : abroadInputDOList) {
                        if (abroadInputDOTemp.getProvincename().contains(fixTool.provinceUni(covData.getProvincename()))) {
                            abroadInputDO = abroadInputDOTemp;
                            break;
                        }
                    }
                    for (AbroadInputDO abroadInputDOTemp : abroadInputDOListYD) {
                        if (abroadInputDOTemp.getProvincename().contains(fixTool.provinceUni(covData.getProvincename()))) {
                            abroadInputDOYD = abroadInputDOTemp;
                            break;
                        }
                    }
                    areaInfo.setAbroadInput(Math.max((abroadInputDO == null ? 0 : abroadInputDO.getThenumber() - (abroadInputDOYD == null ? 0 : abroadInputDOYD.getThenumber())), 0));
                }

                dayInfoResponse.getProvinceInfoList().add(areaInfo);
            }

            return ResultTool.success(dayInfoResponse);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 每日获取数据量
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/4/26
     */
    @Override
    public Result statistic() {
        StatisticResponse statisticResponse = new StatisticResponse();
        StatisticDOExample statisticDOExample = new StatisticDOExample();
        statisticDOExample.createCriteria().andUpdateTimeEqualTo(TimeTool.todayCreate().getTime());
        List<StatisticDO> statisticDOList = statisticDOMapper.selectByExample(statisticDOExample);
        for (StatisticDO statisticDO : statisticDOList) {
            if (statisticDO.getName().equals("covData")) {
                statisticResponse.setCovData(statisticDO.getValue());
            } else if (statisticDO.getName().equals("abroadInput")) {
                statisticResponse.setAbroadInput(statisticDO.getValue());
            }
        }

        if (statisticResponse.getAbroadInput() == null) {
            statisticResponse.setAbroadInput(0);
        }
        if (statisticResponse.getCovData() == null) {
            statisticResponse.setCovData(0);
        }

        statisticResponse.setSumNumber(statisticResponse.getAbroadInput() + statisticResponse.getCovData());
        return ResultTool.success(statisticResponse);
    }
}
