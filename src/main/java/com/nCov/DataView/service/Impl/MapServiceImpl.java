package com.nCov.DataView.service.Impl;

import com.nCov.DataView.dao.AreaDOMapper;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.DayInfo;
import com.nCov.DataView.model.response.info.DayInfoResponse;
import com.nCov.DataView.model.response.info.ProvinceInfo;
import com.nCov.DataView.model.response.info.ProvinceInfoResponse;
import com.nCov.DataView.service.MapService;
import com.nCov.DataView.tools.ResultTool;
import com.nCov.DataView.tools.TimeTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

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
                    ProvinceInfo provinceInfo = new ProvinceInfo();
                    provinceInfo.setConfirm(dayInfoNeed.getConfirm());
                    provinceInfo.setName(provinceInfoResponse.getName());
                    dayInfoResponse.getProvinceInfoList().add(provinceInfo);
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
     * @Description: 时间补全
     * @Param: [provinceDayInfoList, dateNeed]
     * @return: void
     * @Author: SoCMo
     * @Date: 2020/2/28
     */
    private void addIn(List<DayInfo> dayInfoList, Date dateNeed) throws ParseException {
        while (TimeTool.dayDiffStr(dayInfoList.get(dayInfoList.size() - 1).getDate(), TimeTool.timeToDaySy(dateNeed)) != 1) {
            DayInfo dayInfo = new DayInfo();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(TimeTool.stringToDay(dayInfoList.get(dayInfoList.size() - 1).getDate()));
            calendar.add(Calendar.DATE, 1);

            dayInfo.setDate(TimeTool.timeToDaySy(calendar.getTime()));
            dayInfo.setConfirm(dayInfoList.get(dayInfoList.size() - 1).getConfirm());
            dayInfoList.add(dayInfo);
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
        CovDataExample covDataExample = new CovDataExample();
        covDataExample.createCriteria().andIsprovinceEqualTo(1);
        covDataExample.setOrderByClause("date ASC");
        List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
        if (covDataList.isEmpty()) {
            throw new AllException(EmAllException.DATABASE_ERROR, "省信息不存在");
        }

        Map<String, ProvinceInfoResponse> provinceResMap = new HashMap<>();
        for (CovData covData : covDataList) {
            ProvinceInfoResponse provinceInfoResponse = provinceResMap.get(covData.getProvincename());
            if (provinceInfoResponse == null) {
                provinceResMap.put(covData.getProvincename(), new ProvinceInfoResponse());
                provinceInfoResponse = provinceResMap.get(covData.getProvincename());
                provinceInfoResponse.setName(covData.getProvincename());
                provinceInfoResponse.setDayInfoList(new ArrayList<>());
            }

            if (!provinceInfoResponse.getDayInfoList().isEmpty()) {
                List<DayInfo> dayInfoList = provinceInfoResponse.getDayInfoList();
                addIn(dayInfoList, covData.getDate());

                DayInfo dayInfo = new DayInfo();
                dayInfo.setDate(TimeTool.timeToDaySy(covData.getDate()));
                dayInfo.setConfirm(covData.getTotalconfirm());
                dayInfoList.add(dayInfo);
            } else {
                DayInfo dayInfo = new DayInfo();
                dayInfo.setConfirm(covData.getTotalconfirm());
                dayInfo.setDate(TimeTool.timeToDaySy(covData.getDate()));
                provinceInfoResponse.getDayInfoList().add(dayInfo);
            }
        }

        List<ProvinceInfoResponse> provinceInfoResponseList = new ArrayList<>(provinceResMap.values());
        for (ProvinceInfoResponse provinceInfoResponse : provinceInfoResponseList) {
            List<DayInfo> dayInfoList = provinceInfoResponse.getDayInfoList();
            DayInfo dayInfoLast = dayInfoList.get(dayInfoList.size() - 1);
            if (TimeTool.dayDiffDate(TimeTool.stringToDay(dayInfoLast.getDate()), new Date()) != 0) {
                addIn(dayInfoList, new Date());
                DayInfo dayInfo = new DayInfo();
                dayInfo.setDate(TimeTool.timeToDaySy(new Date()));
                dayInfo.setConfirm(dayInfoLast.getConfirm());
                dayInfoList.add(dayInfo);
            }
        }


        provinceResMap = null;
        return provinceInfoResponseList;
    }
}
