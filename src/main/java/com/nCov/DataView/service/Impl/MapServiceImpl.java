package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.AreaDOMapper;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.entity.AreaDO;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.AreaInfo;
import com.nCov.DataView.model.response.info.DayInfo;
import com.nCov.DataView.model.response.info.DayInfoResponse;
import com.nCov.DataView.model.response.info.ProvinceInfoResponse;
import com.nCov.DataView.service.MapService;
import com.nCov.DataView.tools.ResultTool;
import com.nCov.DataView.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MapServiceImpl implements MapService {
    @Resource
    private AreaDOMapper areaDOMapper;

    @Resource
    private CovDataMapper covDataMapper;

    /**
     * @Description: 各个省每日疫情数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    @Override
    public Result provinceInfo() {
        try {
            return ResultTool.success(epidemicInfo());
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 每日各个省疫情数据
     * @Param: []
     * @return: com.nCov.DataView.model.response.Result
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    @Override
    public Result dateInfo() {
        try {
            List<ProvinceInfoResponse> provinceInfoResponseList = epidemicInfo();
            Calendar calendarNow = Calendar.getInstance();

            Calendar calendarTraver = new GregorianCalendar();
            calendarTraver.set(2020, Calendar.JANUARY, 24, 0, 0, 0);

            List<DayInfoResponse> dayInfoResList = new ArrayList<>();
            while (!calendarTraver.after(calendarNow)) {
                DayInfoResponse dayInfoResponse = new DayInfoResponse();
                dayInfoResponse.setDate(TimeTool.timeToDaySy(calendarTraver.getTime()));
                dayInfoResponse.setProvinceInfoList(new ArrayList<>());

                for (ProvinceInfoResponse provinceInfoResponse : provinceInfoResponseList) {
                    DayInfo dayInfoNeed = new DayInfo();
                    dayInfoNeed.setDate(TimeTool.timeToDaySy(calendarTraver.getTime()));
                    dayInfoNeed.setConfirm(0);

                    for (DayInfo dayInfo : provinceInfoResponse.getDayInfoList()) {
                        if (TimeTool.dayDiffStr(dayInfo.getDate(), TimeTool.timeToDaySy(calendarTraver.getTime())) < 0) {
                            break;
                        } else {
                            dayInfoNeed = dayInfo;
                        }
                    }
                    AreaInfo areaInfo = new AreaInfo();
                    areaInfo.setConfirm(dayInfoNeed.getConfirm());
                    areaInfo.setName(provinceInfoResponse.getName());
                    dayInfoResponse.getProvinceInfoList().add(areaInfo);
                }
                dayInfoResList.add(dayInfoResponse);
                calendarTraver.add(Calendar.DATE, 1);
            }

            return ResultTool.success(dayInfoResList);
        } catch (AllException e) {
            log.error(e.getMsg());
            return ResultTool.error(e.getErrCode(), e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResultTool.error(500, e.getMessage());
        }
    }

    /**
     * @Description: 各个省每日疫情数据
     * @Param: []
     * @return: java.util.List<com.nCov.DataView.model.response.info.CNMapResponse>
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    public List<ProvinceInfoResponse> epidemicInfo() throws ParseException, AllException {
        Map<String, AreaDO> ProvinceMap = areaDOMapper.getProvinceMapString();
        List<CovData> covDataList = covDataMapper.getInfoProvince();
        List<ProvinceInfoResponse> provinceInfoList = new ArrayList<>();
        for (AreaDO areaDO : ProvinceMap.values()) {
            Map<String, CovData> tempProList = covDataList.stream()
                    .filter(covData -> covData.getAreaname().contains(areaDO.getName()))
                    .collect(Collectors.toMap(covData -> TimeTool.timeToDaySy(covData.getDate()), covData -> covData));
            Calendar calendar = Calendar.getInstance();
            calendar.set(2020, Calendar.JANUARY, 24, 0, 0, 0);
            CovData temp = null;
            String areaName = null;
            ProvinceInfoResponse provinceInfoResponse = new ProvinceInfoResponse();
            provinceInfoResponse.setDayInfoList(new ArrayList<>());
            while (calendar.getTime().before(new Date())) {
                if (tempProList.get(TimeTool.timeToDaySy(calendar.getTime())) == null) {
                    if (temp == null) {
                        CovData covData = new CovData();
                        covData.setDate(calendar.getTime());
                        covData.setAreaname(areaDO.getName());
                        covData.setProvincename(areaDO.getName());
                        covData.setIsprovince(1);
                        covData.setTotalconfirm(0);
                        covDataMapper.insertSelective(covData);

                        DayInfo dayInfo = new DayInfo();
                        dayInfo.setDate(TimeTool.timeToDaySy(calendar.getTime()));
                        int remain = covData.getTotalconfirm() - covData.getTotalheal() - covData.getTotaldead();
                        dayInfo.setConfirm(Math.max(remain, 0));
                        provinceInfoResponse.getDayInfoList().add(dayInfo);
                    } else {
                        CovData covData = new CovData();
                        BeanUtils.copyProperties(temp, covData);
                        covData.setId(null);
                        covData.setDate(calendar.getTime());
                        covDataMapper.insertSelective(covData);

                        DayInfo dayInfo = new DayInfo();
                        dayInfo.setDate(TimeTool.timeToDaySy(calendar.getTime()));
                        int remain = covData.getTotalconfirm() - covData.getTotalheal() - covData.getTotaldead();
                        dayInfo.setConfirm(Math.max(remain, 0));
                        provinceInfoResponse.getDayInfoList().add(dayInfo);
                    }
                    calendar.add(Calendar.DATE, 1);
                } else {
                    DayInfo dayInfo = new DayInfo();
                    dayInfo.setDate(TimeTool.timeToDaySy(calendar.getTime()));
                    CovData covData = tempProList.get(TimeTool.timeToDaySy(calendar.getTime()));
                    int remain = covData.getTotalconfirm() - covData.getTotalheal() - covData.getTotaldead();
                    dayInfo.setConfirm(remain);
                    provinceInfoResponse.getDayInfoList().add(dayInfo);
                    temp = tempProList.get(TimeTool.timeToDaySy(calendar.getTime()));
                    if (areaName == null) areaName = temp.getProvincename();
                    calendar.add(Calendar.DATE, 1);
                }
            }
            provinceInfoResponse.setName(areaName);
            provinceInfoList.add(provinceInfoResponse);
        }

        return provinceInfoList;
    }
}
