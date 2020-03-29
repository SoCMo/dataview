package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
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
//    @Scheduled(cron = "0 0 */3 * * * *")
    public void dataFix(String date) {
        //api地址
        String url = "http://lab.isaaclin.cn/nCoV/api/area?latest=1";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            JSONArray jsonArray = JSON.parseArray(EntityUtils.toString(responseEntity));

            List<CovData> covDataList = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray cityArray = jsonObject.getJSONArray("citys");
                if (jsonObject.getString("countryName").equals("中国") && !cityArray.isEmpty() && TimeTool.timeToDaySy(jsonObject.getDate("updateTime")).equals(TimeTool.timeToDaySy(TimeTool.todayCreate().getTime()))) {
                    for (int j = 0; j < jsonArray.size(); j++) {
                        JSONObject city = cityArray.getJSONObject(j);

                    }
                }
            }
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
