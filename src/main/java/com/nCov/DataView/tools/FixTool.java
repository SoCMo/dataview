package com.nCov.DataView.tools;

import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * program: FixTool
 * description: 数据补充工具
 * author: SoCMo
 * create: 2020/3/16
 */
@Component
public class FixTool {
    @Resource
    CovDataMapper covDataMapper;

    /**
     * @Description: 疫情数据拟合
     * @Param: [Date, name]
     * @return: com.nCov.DataView.model.entity.CovData
     * @Author: SoCMo
     * @Date: 2020/3/16
     */
    public CovData fixCovDate(String date, String name) throws ParseException {
        //TODO 需要修改，存在日期前
        CovDataExample covDataExample = new CovDataExample();
        covDataExample.createCriteria()
                .andAreanameLike(name + "%")
                .andDateLessThanOrEqualTo(TimeTool.stringToDay(date));
        covDataExample.setOrderByClause("Date DESC");
        List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
        if (covDataList.isEmpty()) return null;

        if (covDataList.get(0).getDate().compareTo(TimeTool.stringToDay(date)) != 0) {
            CovData covData = new CovData();
            BeanUtils.copyProperties(covDataList.get(0), covData);
            covData.setDate(TimeTool.stringToDay(date));
            covData.setId(null);
            covDataMapper.insertSelective(covData);
        }
        return covDataList.get(0);
    }

    /**
     * @Description: 地级市名称统一，去除后缀工具
     * @Param: [area]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/3/18
     */
    public String areaUni(String area) {
        String regex = "(.+)[区市县]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(area);
        if (matcher.find()) {
            if (!area.equals("浦东新区") && !area.equals("滨海新区") && !area.contains("神农架林区")) {
                return matcher.group(1);
            }
        }

        regex = "(.+)自治州$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(area);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return area;
    }
}
