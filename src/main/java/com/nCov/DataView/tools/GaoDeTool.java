package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.response.info.SiteAndAreaInfo;
import com.nCov.DataView.model.response.info.SiteInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * program: GaoDeTool
 * description: 高德api交互
 * author: pongshy
 * create: 2020/4/28
 */
@Component
@Slf4j
public class GaoDeTool {
    @Value("#{'${gaoKey}'}")
    private String key;

    @Resource
    private BaiduTool baiduTool;

    /**
     * @Description: 正地址api
     * @Param: [address]
     * @return: java.util.Map<java.lang.String, java.lang.Double>
     * @Author: pongshy
     * @Date: 2020/4/28
     */
    public Map<String, Double> getCoding(String address) throws AllException, IOException {
        String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=json&key=" + key;
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        response = closeableHttpClient.execute(httpGet);

        HttpEntity httpEntity = response.getEntity();
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
        if (jsonObject.getString("status").equals("1")) {
            JSONArray jsonArray = jsonObject.getJSONArray("geocodes");
            Map<String, Double> locationMap = new HashMap<>();

            String[] addressList = jsonArray.getJSONObject(0).getString("location").split(",");
            locationMap.put("lng", Double.parseDouble(addressList[0]));
            locationMap.put("lat", Double.parseDouble(addressList[1]));

//            System.out.println(jsonArray.getJSONObject(0).getString("city"));

            response.close();
            closeableHttpClient.close();

            return locationMap;
        } else {
          throw new AllException(EmAllException.GAODE_REQUEST_FAIL, "高德api报错:" + jsonObject.getString("info"));
        }
    }

    /**
     * @Description: 获取地址的城市
     * @Param: [address]
     * @return: java.util.String
     * @Author: pongshy
     * @Date: 2020/4/29
     */
    public String getCity(String address) throws AllException, IOException {
        String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=json&key=" + key;
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        response = closeableHttpClient.execute(httpGet);

        HttpEntity httpEntity = response.getEntity();
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
        if (jsonObject.getString("status").equals("1")) {
            JSONArray jsonArray = jsonObject.getJSONArray("geocodes");

            String city = jsonArray.getJSONObject(0).getString("city");

            response.close();
            closeableHttpClient.close();

            return city;
        } else {
            throw new AllException(EmAllException.GAODE_REQUEST_FAIL, "高德api报错:" + jsonObject.getString("info"));
        }
    }

    /**
     * @Description: 获取地址的所处的区或城市
     * @Param: [address]
     * @return: java.util.String
     * @Author: pongshy
     * @Date: 2020/4/29
     */
    public String getAreaOrCity(String address) throws AllException, IOException {
        if (address.equals("上海市上海大学")) {
            address = "上海大学宝山校区";
        }
        String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address + "&output=json&key=" + key;
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        response = closeableHttpClient.execute(httpGet);

        HttpEntity httpEntity = response.getEntity();
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
        if (jsonObject.getString("status").equals("1")) {
            JSONArray jsonArray = jsonObject.getJSONArray("geocodes");

            String city = jsonArray.getJSONObject(0).getString("district");
            if (city.equals("[]")) {
                city = jsonArray.getJSONObject(0).getString("city");
            }

//            System.out.println(address + ": " + city);

            response.close();
            closeableHttpClient.close();

            return city;
        } else {
            throw new AllException(EmAllException.GAODE_REQUEST_FAIL, "高德api报错:" + jsonObject.getString("info"));
        }
    }

    /**
     * @Description: 站点获取
     * @Param: [startAddress, endAddress, byTheWay, type_num] type_num: 0 -> 公交; 1 -> 地铁; 3 -> 火车, byTheWay: 具体交通工具，比如地铁2号线，G28
     * @return: com.nCov.DtaView.model.response.info.SiteInfo
     * @Author: pongshy
     * @Date: 2020/4/28
     */
    public SiteInfo getSitesList(String startAddress, String endAddress, String byTheWay, Integer type_num) throws AllException, IOException {
        if (endAddress.contains("上海大学")) {
            endAddress = "上海市上海大学宝山校区";
        }
        Map<String, Double> startCoding =  getCoding(startAddress);
        Map<String, Double> endCoding = getCoding(endAddress);
        String startCity = getCity(startAddress);
        String endCity = getCity(endAddress);

        String url = "https://restapi.amap.com/v3/direction/transit/integrated?origin=" + startCoding.get("lng").toString() +
                "," + startCoding.get("lat").toString() + "&destination=" + endCoding.get("lng").toString() + "," + endCoding.get("lat").toString() +
                "&city=" + startCity + "&cityd=" + endCity + "&extensions=all&strategy=2&output=json&key=" + key;
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        //请求
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        response = closeableHttpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(httpEntity));
        if (jsonObject.getString("status").equals("1")) {
            SiteInfo siteInfo = new SiteInfo();
            siteInfo.setSiteNames(new ArrayList<>());
            String type = null;
            if (type_num == 0) {
                type = "普通公交线路";
            }
            else if (type_num == 1) {
                type = "地铁线路";
            }

            JSONArray jsonArray = jsonObject.getJSONObject("route").getJSONArray("transits");
            List<SiteAndAreaInfo> namesAndArea = new ArrayList<>();
            for (int i = 0; i < jsonArray.size(); ++i) {
                JSONArray segments = jsonArray.getJSONObject(i).getJSONArray("segments");
                for (int j = 0; j < segments.size(); ++j) {
                    JSONObject segment_one = segments.getJSONObject(j);

                    if (type_num == 3) {
                        JSONObject railway = segment_one.getJSONObject("railway");

                        if (railway.getString("trip").equals(byTheWay)) {
                            SiteAndAreaInfo siteAndAreaInfo = new SiteAndAreaInfo();

                            JSONObject departure_stop = railway.getJSONObject("departure_stop");
                            siteAndAreaInfo.setName(departure_stop.getString("name"));
                            siteAndAreaInfo.setArea(departure_stop.getString("name"));
                            namesAndArea.add(siteAndAreaInfo);

                            JSONArray via_stops = railway.getJSONArray("via_stops");
                            for (int p = 0; p < via_stops.size(); ++p) {
                                JSONObject via_temp = via_stops.getJSONObject(p);
                                SiteAndAreaInfo temp = new SiteAndAreaInfo();

                                temp.setName(via_temp.getString("name"));
                                temp.setArea(via_temp.getString("name"));
                                namesAndArea.add(temp);
                            }

                            JSONObject arrival_stop = railway.getJSONObject("arrival_stop");
                            SiteAndAreaInfo siteAndAreaInfo1 = new SiteAndAreaInfo();
                            siteAndAreaInfo1.setName(arrival_stop.getString("name"));
                            siteAndAreaInfo1.setArea(arrival_stop.getString("name"));
                            namesAndArea.add(siteAndAreaInfo1);

                            siteInfo.setSiteNames(namesAndArea);
                            siteInfo.setAllSiteNumber(namesAndArea.size());

                            return siteInfo;
                        }
                    }
                    else if (type_num == 0 || type_num == 1) {
                        JSONObject bus = segment_one.getJSONObject("bus");
                        JSONArray buslines = bus.getJSONArray("buslines");

                        if (!buslines.isEmpty()) {
                            for (int m = 0; m < buslines.size(); ++m) {
                                JSONObject buslines_one = buslines.getJSONObject(m);

                                //获取高德api查询获取到的交通工具名称
                                String getWay = buslines_one.getString("name");
                                if (type_num == 0) {
                                    getWay = getWay.substring(0, getWay.indexOf("("));
                                } else if (type_num == 1) {
                                    getWay = getWay.substring(0, getWay.indexOf("线") + 1);
                                }
                                if (buslines_one.getString("type").equals(type) && byTheWay.contains(getWay)) {
                                    JSONObject departure_stop = buslines_one.getJSONObject("departure_stop");
                                    SiteAndAreaInfo siteAndAreaInfo = new SiteAndAreaInfo();
                                    siteAndAreaInfo.setName(departure_stop.getString("name"));
                                    siteAndAreaInfo.setArea(getAreaOrCity(startCity + departure_stop.getString("name")));
                                    namesAndArea.add(siteAndAreaInfo);

                                    JSONArray via_stops = buslines_one.getJSONArray("via_stops");
                                    for (int k = 0; k < via_stops.size(); ++k) {
                                        SiteAndAreaInfo temp = new SiteAndAreaInfo();
                                        JSONObject via_stops_one = via_stops.getJSONObject(k);

                                        temp.setName(via_stops_one.getString("name"));
                                        temp.setArea(getAreaOrCity(startCity + via_stops_one.getString("name")));
                                        namesAndArea.add(temp);
                                    }

                                    SiteAndAreaInfo siteAndAreaInfo1 = new SiteAndAreaInfo();
                                    JSONObject arrival_stop = buslines_one.getJSONObject("arrival_stop");
                                    siteAndAreaInfo1.setName(arrival_stop.getString("name"));
                                    siteAndAreaInfo1.setArea(getAreaOrCity(startCity + arrival_stop.getString("name")));
                                    namesAndArea.add(siteAndAreaInfo1);

                                    siteInfo.setSiteNames(namesAndArea);
                                    siteInfo.setAllSiteNumber(namesAndArea.size());

                                    return siteInfo;
                                }
                            }
                        }
                    }

                }
            }
            return null;
            //throw new AllException(EmAllException.GAODE_REQUEST_FAIL, "无法检索到该交通方式下输入地址之间的站名");
        } else {
            System.out.println(jsonObject.getString("info"));
            log.info("调用高德api失败");
            throw new AllException(EmAllException.GAODE_REQUEST_FAIL, "高德api报错：" + jsonObject.getString("info"));
        }

    }
}
