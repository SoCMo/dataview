package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
@EnableScheduling
@Slf4j
public class FixTool {
    @Resource
    private CovDataMapper covDataMapper;

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

    /**
     * @Description: 疫情信息补充
     * @Param: [date]
     * @return: java.util.List<com.nCov.DataView.model.entity.CovData>
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
//    @Scheduled(cron = "0 0 0/3 * * *")
    public void dataFix() {
        //api地址
        String url = "http://lab.isaaclin.cn/nCoV/api/area?latest=1";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        log.info("成功获取数据仓库信息，开始更新数据！");
        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            JSONArray jsonArray = JSON.parseObject(EntityUtils.toString(responseEntity)).getJSONArray("results");

            List<CovData> covDataListInsert = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject provinceData = jsonArray.getJSONObject(i);
                JSONArray cityArray = provinceData.getJSONArray("cities");
                if (provinceData.getString("countryName").equals("中国") && cityArray != null && TimeTool.timeToDaySy(provinceData.getDate("updateTime")).equals(TimeTool.timeToDaySy(new Date()))) {

                    //插入或更新省的数据
                    CovDataExample covDataExamplePro = new CovDataExample();
                    covDataExamplePro.createCriteria()
                            .andIsprovinceEqualTo(1)
                            .andAreanameEqualTo(provinceData.getString("provinceName"))
                            .andProvincenameEqualTo(provinceData.getString("provinceName"))
                            .andDateEqualTo(TimeTool.todayCreate().getTime());

                    List<CovData> covDataListPro = covDataMapper.selectByExample(covDataExamplePro);
                    if (covDataListPro.size() > 1) {
                        log.error(provinceData.getString("provinceName") + "数据有误，数据库中多于两个");
                    } else if (covDataListPro.isEmpty()) {
                        CovData covData = new CovData();
                        covData.setIsprovince(1);
                        covData.setProvincename(provinceData.getString("provinceName"));
                        covData.setAreaname(provinceData.getString("provinceName"));
                        covData.setDate(TimeTool.todayCreate().getTime());
                        covData.setTotalconfirm(provinceData.getInteger("confirmedCount"));
                        covData.setTotalsuspect(provinceData.getInteger("suspectedCount"));
                        covData.setTotalheal(provinceData.getInteger("curedCount"));
                        covData.setTotaldead(provinceData.getInteger("deadCount"));
                        covDataListInsert.add(covData);
                    } else {
                        CovData covData = new CovData();
                        covData.setIsprovince(1);
                        covData.setProvincename(provinceData.getString("provinceName"));
                        covData.setAreaname(provinceData.getString("cityName"));
                        covData.setDate(TimeTool.todayCreate().getTime());
                        covData.setTotalconfirm(provinceData.getInteger("confirmedCount"));
                        covData.setTotalsuspect(provinceData.getInteger("suspectedCount"));
                        covData.setTotalheal(provinceData.getInteger("curedCount"));
                        covData.setTotaldead(provinceData.getInteger("deadCount"));
                        covDataMapper.updateByExampleSelective(covData, covDataExamplePro);
                    }


                    for (int j = 0; j < cityArray.size(); j++) {
                        JSONObject cityData = cityArray.getJSONObject(j);
                        CovDataExample covDataExample = new CovDataExample();
                        covDataExample.createCriteria()
                                .andProvincenameEqualTo(provinceData.getString("provinceName"))
                                .andAreanameEqualTo(cityData.getString("cityName"))
                                .andIsprovinceEqualTo(0)
                                .andDateEqualTo(TimeTool.todayCreate().getTime());
                        List<CovData> covDataList = covDataMapper.selectByExample(covDataExample);
                        if (covDataList.size() > 1) {
                            log.error(provinceData.getString("provinceName") + cityData.getString("cityName") + "数据有误，数据库中多于两个");
                        } else if (covDataList.isEmpty()) {
                            CovData covData = new CovData();
                            covData.setIsprovince(0);
                            covData.setProvincename(provinceData.getString("provinceName"));
                            covData.setAreaname(cityData.getString("cityName"));
                            covData.setDate(TimeTool.todayCreate().getTime());
                            covData.setTotalconfirm(cityData.getInteger("confirmedCount"));
                            covData.setTotalsuspect(cityData.getInteger("suspectedCount"));
                            covData.setTotalheal(cityData.getInteger("curedCount"));
                            covData.setTotaldead(cityData.getInteger("deadCount"));
                            covDataListInsert.add(covData);
                        } else {
                            CovData covData = new CovData();
                            covData.setIsprovince(0);
                            covData.setProvincename(provinceData.getString("provinceName"));
                            covData.setAreaname(cityData.getString("cityName"));
                            covData.setDate(TimeTool.todayCreate().getTime());
                            covData.setTotalconfirm(cityData.getInteger("confirmedCount"));
                            covData.setTotalsuspect(cityData.getInteger("suspectedCount"));
                            covData.setTotalheal(cityData.getInteger("curedCount"));
                            covData.setTotaldead(cityData.getInteger("deadCount"));
                            covDataMapper.updateByExampleSelective(covData, covDataExample);
                        }
                    }
                }
            }
            covDataMapper.insertList(covDataListInsert);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
