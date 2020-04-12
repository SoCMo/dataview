package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.dao.*;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.ConstCorrespond;
import com.nCov.DataView.model.entity.*;
import com.nCov.DataView.model.response.info.CovRankResponse;
import com.nCov.DataView.service.Impl.EpidemicServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private AreaDOMapper areaDOMapper;

    @Resource
    private CovDataMapper covDataMapper;

    @Resource
    private PathInfoDOMapper pathInfoDOMapper;

    @Resource
    private PassInfoDOMapper passInfoDOMapper;

//    @Resource
//    private AssessDOMapper assessDOMapper;
//
//    @Resource
//    private EpidemicServiceImpl epidemicServiceImpl;

    @Resource
    private BaiduTool baiduTool;

    /**
     * @Description: 疫情数据拟合
     * @Param: [Date, name]
     * @return: com.nCov.DataView.model.entity.CovData
     * @Author: SoCMo
     * @Date: 2020/3/16
     */
    public CovData fixCovDate(String date, String name) throws ParseException {
        if (TimeTool.stringToDay(date).before(TimeTool.stringToDay("2020-01-24"))) {
            return null;
        }

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
     * @Description: 省级名称统一工具，去后缀
     * @Param: [area]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/4/4
     */
    public String provinceUni(String area) {
        String regex = "(.+)[市省]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(area);
        if (matcher.find()) {
            return matcher.group(1);
        }

        regex = "(.+)自治区$";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(area);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return area;
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
    @Scheduled(cron = "0 0 0/3 * * *")
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
                        covData.setAreaname(provinceData.getString("provinceName"));
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
            if (!covDataListInsert.isEmpty())
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


    /**
     * @Description: area表经纬度补充
     * @Param: []
     * @return: void
     * @Author: SoCMo
     * @Date: 2020/4/12
     */
    public void LngAndLatFix() throws IOException, AllException {
        AreaDOExample areaDOExample = new AreaDOExample();
        areaDOExample.createCriteria()
                .andIdGreaterThan(35)
                .andLngEqualTo(0.0)
                .andParentidNotEqualTo(32);
        List<AreaDO> areaDOList = areaDOMapper.selectByExample(areaDOExample);

        Map<Integer, AreaDO> integerProvinceMap = areaDOMapper.getProvinceMapInt();
        for (AreaDO areaDO : areaDOList) {
            Map<String, Double> lngAndLatMap = null;
            List<String> someName = Arrays.asList("垫江", "忠县");
            if (someName.contains(areaDO.getName())) lngAndLatMap = baiduTool.geoCoding(areaDO.getName());
            else if (areaDO.getName().equals("苗栗")) continue;
            else
                lngAndLatMap = baiduTool.geoCoding(integerProvinceMap.get(areaDO.getParentid()).getName() + areaDO.getName());
            AreaDO areaDOTemp = new AreaDO();
            areaDOTemp.setLng(lngAndLatMap.get("lng"));
            areaDOTemp.setLat(lngAndLatMap.get("lat"));
            areaDOExample.clear();
            areaDOExample.createCriteria().andIdEqualTo(areaDO.getId());
            areaDOMapper.updateByExampleSelective(areaDOTemp, areaDOExample);
        }
    }

    /**
     * @Description: 每天对各路段进行评估
     * @Param: []
     * @return: void
     * @Author: SoCMo
     * @Date: 2020/4/10
     */
    /*
    @Scheduled(cron = "0 2 0 * * *")
    public void assess() {
        try {
            //获取path表信息
            PathInfoDOExample pathInfoDOExample = new PathInfoDOExample();
            List<PathInfoDO> pathInfoDOList = pathInfoDOMapper.selectByExample(pathInfoDOExample);
            if (pathInfoDOList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "pathInfo表为空");
            }

            //获取pass表信息
            PassInfoDOExample passInfoDOExample = new PassInfoDOExample();
            passInfoDOExample.setOrderByClause("order_id ASC, id ASC");
            List<PassInfoDO> passInfoDOList = passInfoDOMapper.selectByExample(passInfoDOExample);
            if (passInfoDOList.isEmpty()) {
                throw new AllException(EmAllException.DATABASE_ERROR, "passInfo表为空");
            }

            //获取impArea的信息
            ImpAreaDOExample impAreaDOExample = new ImpAreaDOExample();
            Calendar calendar = TimeTool.todayCreate();
            List<CovRankResponse> covDataList = epidemicServiceImpl.allAreaCal(TimeTool.timeToDaySy(calendar.getTime()));
            Map<String, CovRankResponse> covRankResponseMap = covDataList.stream().collect(Collectors.toMap(CovRankResponse::getName, covRankResponse -> covRankResponse));

            //计算
            for (PathInfoDO pathInfoDO : pathInfoDOList) {
                List<PassInfoDO> passInfoDOS = passInfoDOList.stream().filter(passInfoDO -> passInfoDO.getPathId().equals(pathInfoDO.getId())).collect(Collectors.toList());
                int order = 0;
                List<AssessDO> sumList = new ArrayList<>();
                List<AssessDO> finalList = new ArrayList<>();
                for (PassInfoDO passInfoDO : passInfoDOS) {
                    if (order < passInfoDO.getOrderId()) {
                        double localScore = finalList.stream().mapToDouble(AssessDO::getLocalScore).max().getAsDouble();
                        for (AssessDO assessDO : finalList) {
                            assessDO.setFinalScore(localScore);
                            sumList.add(assessDO);
                        }
                        order++;
                    }
                    AssessDO assessDO = new AssessDO();
                    assessDO.setAreaName(passInfoDO.getArea());
                    assessDO.setPathId(pathInfoDO.getId());
                    assessDO.setPassOrder(passInfoDO.getOrderId());

                    assessDO.setCleanlinessScore((int) ConstCorrespond.CLEAN_SCORE[passInfoDO.getTypeNum()]);
                    assessDO.setCrowdScore((int) ConstCorrespond.CROWD[passInfoDO.getTypeNum()]);

                    CovRankResponse covRankResponse = covRankResponseMap.get(this.areaUni(passInfoDO.getArea()));
                    if (covRankResponse == null) {
                        covRankResponse = covRankResponseMap.get(passInfoDO.getArea());
                        if (covRankResponse == null) {
                            for (CovRankResponse covRankResponseTemp : covRankResponseMap.values()) {
                                if (covRankResponseTemp.getName().contains(this.areaUni(passInfoDO.getArea()))) {
                                    covRankResponse = covRankResponseTemp;
                                    break;
                                }
                            }
                            if (covRankResponse == null) {
                                throw new AllException(EmAllException.DATABASE_ERROR, passInfoDO.getArea() + "无风险数据");
                            }
                        }
                    }
                    assessDO.setLocalScore((int) covRankResponse.getSumScore());
                }
            }
        } catch (
                AllException e) {
            log.error(e.getMsg());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

     */
}


