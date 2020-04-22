package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
import com.nCov.DataView.model.ConstCorrespond;
import com.nCov.DataView.model.entity.RouteInfo;
import com.nCov.DataView.model.request.RouteCalRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * program: BaiduTool
 * description: 百度api交互
 * author: SoCMo
 * create: 2020/3/30
 */
@Component
@Slf4j
public class BaiduTool {
    @Value("#{'${baiAK}'.split(',')}")
    private List<String> akList;

    /**
     * @Description: 逆地址api
     * @Param: [lat, lng]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    public String reverseGeoCoding(double lat, double lng) throws AllException, IOException {
        int akNumber = 0;

        while (akNumber < akList.size()) {
            //api地址
            String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + akList.get(akNumber) + "&output=json&coordtype=bd09ll&location="
                    + lat + "," + lng;
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 请求
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(responseEntity));
            if (jsonObject.getInteger("status") == 301 || jsonObject.getInteger("status") == 302) {
                akNumber++;
            } else if (jsonObject.getInteger("status") == 0) {
                String city = jsonObject.getJSONObject("result")
                        .getJSONObject("addressComponent")
                        .getString("city");
                return city.contains("上海") || city.contains("天津") || city.contains("北京") || city.contains("重庆")
                        ? jsonObject.getJSONObject("result")
                        .getJSONObject("addressComponent")
                        .getString("district") : city;
            } else {
                throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "百度api报错为" + jsonObject.getInteger("status"));
            }
            response.close();
            httpClient.close();
        }
        throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "配额已到上限");
    }

    /**
     * @Description: 正地址api
     * @Param: [address]
     * @return: java.util.Map<java.lang.String, java.lang.Double>
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    public Map<String, Double> geoCoding(String address) throws AllException, IOException {
        int akNumber = 0;

        while (akNumber < akList.size()) {
            //api地址
            String url = "http://api.map.baidu.com/geocoding/v3/?address="
                    + address
                    + "&output=json&ak=" + akList.get(akNumber);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            // 请求
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(responseEntity));
            if (jsonObject.getString("status").matches("3[0-9][0-9]")) {
                akNumber++;
            } else if (jsonObject.getInteger("status") == 0) {
                JSONObject location = jsonObject.getJSONObject("result")
                        .getJSONObject("location");
                Map<String, Double> locationMap = new HashMap<>();
                locationMap.put("lng", location.getDouble("lng"));
                locationMap.put("lat", location.getDouble("lat"));
                return locationMap;
            } else {
                throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "百度api报错为" + jsonObject.getInteger("status"));
            }
            response.close();
            httpClient.close();
        }
        throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "配额已到上限");
    }

    /**
     * @Description: 多线路查询
     * @Param: [start, end]
     * @return: com.nCov.DataView.model.request.PathRequest
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    public List<RouteInfo> pathInfo(String start, String end) throws IOException, AllException {
        boolean exist = false;
        List<RouteInfo> aircraft = routeListInfo(start, end, 1);
        if (!aircraft.isEmpty()) {
            exist = true;
        }

        List<RouteInfo> train = routeListInfo(start, end, 0);
        if (!train.isEmpty()) {
            exist = true;
        }

        if (exist) {
            List<RouteInfo> routeInfoList = new ArrayList<>();
            routeInfoList.addAll(aircraft);
            routeInfoList.addAll(train);
            return routeInfoList;
        } else {
            return routeListInfo(start, end, 3);
        }
    }

    /**
     * @Description: 单路线
     * @Param: [start, end]
     * @return: com.nCov.DataView.model.request.PathRequest
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    public List<RouteInfo> routeListInfo(String start, String end, Integer type) throws AllException, IOException {
        int akNumber = 0;
        Map<String, Double> startMap = this.geoCoding(start);
        Map<String, Double> endMap = this.geoCoding(end);
        while (akNumber < akList.size()) {
            //api地址
            String url = "http://api.map.baidu.com/direction/v2/transit?origin="
                    + startMap.get("lat") + "," + startMap.get("lng") + "&destination="
                    + endMap.get("lat") + "," + endMap.get("lng") + "&ak="
                    + akList.get(akNumber) + "&tactics_incity=5&trans_type_intercity=" + type;
            CloseableHttpClient httpClient = HttpClients.createDefault();

            //请求
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();
            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(responseEntity));
            if (jsonObject.getString("status").matches("3[0-9][0-9]")) {
                akNumber++;
            } else if (jsonObject.getString("status").matches("1001")) {
                throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "没有公交方案");
            } else if (jsonObject.getInteger("status") == 0) {
                int methodNum = 0;
                List<RouteInfo> routeInfoList = new ArrayList<>();
                JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("routes");

                while (methodNum < 3 && methodNum < jsonArray.size()) {
                    JSONObject route = jsonArray.getJSONObject(methodNum++);
                    JSONArray steps = route.getJSONArray("steps");

                    //验证类型
                    if (type < 2) {
                        boolean exist = false;
                        for (int j = 0; j < steps.size(); j++) {
                            JSONArray childSteps = steps.getJSONArray(j);
                            for (int z = 0; z < childSteps.size(); z++) {
                                JSONObject finalSteps = childSteps.getJSONObject(z);
                                JSONObject vehicle = finalSteps.getJSONObject("vehicle_info");
                                if (vehicle.getInteger("type") == type + 1) {
                                    exist = true;
                                    break;
                                }
                            }
                            if (exist) break;
                        }

                        if (!exist) break;
                    }

                    RouteInfo routeInfo = new RouteInfo();
                    routeInfo.setSumTime(route.getDouble("duration") / 60.0 / 60.0);
                    routeInfo.setRouteCalRequestList(new ArrayList<>());

                    for (int j = 0; j < steps.size(); j++) {
                        JSONArray childSteps = steps.getJSONArray(j);
                        for (int z = 0; z < childSteps.size(); z++) {
                            JSONObject finalSteps = childSteps.getJSONObject(z);
                            if (finalSteps.getString("instructions").isEmpty()) continue;
                            RouteCalRequest routeCalRequest = new RouteCalRequest();
                            JSONObject vehicle = finalSteps.getJSONObject("vehicle_info");
                            switch (vehicle.getInteger("type")) {
                                //火车路线
                                case 1: {
                                    routeCalRequest.setType(ConstCorrespond.PATH_INFO_TYPE[0]);
                                    List<String> cities = new ArrayList<>();
                                    String path = finalSteps.getString("path");
                                    String[] points = path.split(";");
                                    double sumDistance = 0;
                                    String[] lngLat = null;
                                    for (int pointIndex = 0; pointIndex < points.length; pointIndex++) {
                                        if (pointIndex == 0) {
                                            lngLat = points[pointIndex].split(",");
                                        }
                                        String city = "";
                                        if (pointIndex < points.length - 1) {
                                            String[] temp = points[pointIndex + 1].split(",");
                                            //计算距离
                                            if (pointIndex < points.length - 1) {
                                                sumDistance += NumberTool.getDistance(Double.parseDouble(lngLat[1]), Double.parseDouble(lngLat[0]),
                                                        Double.parseDouble(temp[1]), Double.parseDouble(temp[0]));
                                            }
                                            city = this.reverseGeoCoding(Double.parseDouble(lngLat[1]), Double.parseDouble(lngLat[0]));
                                            lngLat = temp;
                                        } else
                                            city = this.reverseGeoCoding(Double.parseDouble(lngLat[1]), Double.parseDouble(lngLat[0]));

                                        if (!cities.contains(city) && !city.isEmpty()) cities.add(city);
                                    }
                                    routeCalRequest.setCitys(cities);
                                    JSONObject detail = vehicle.getJSONObject("detail");
                                    routeCalRequest.setStart(detail.getString("departure_station"));
                                    routeCalRequest.setEnd(detail.getString("arrive_station"));
                                    routeCalRequest.setTitle(detail.getString("name"));
                                    routeCalRequest.setDistance(sumDistance);
                                    break;
                                }
                                case 2: {
                                    //飞机路线
                                    routeCalRequest.setDistance(finalSteps.getInteger("distance"));
                                    routeCalRequest.setType(ConstCorrespond.PATH_INFO_TYPE[1]);
                                    List<String> cities = new ArrayList<>();
                                    JSONObject startJson = finalSteps.getJSONObject("start_location");
                                    JSONObject endJson = finalSteps.getJSONObject("end_location");
                                    cities.add(this.reverseGeoCoding(startJson.getDouble("lat"), startJson.getDouble("lng")));
                                    cities.add(this.reverseGeoCoding(endJson.getDouble("lat"), endJson.getDouble("lng")));
                                    routeCalRequest.setCitys(cities);

                                    JSONObject detail = vehicle.getJSONObject("detail");
                                    routeCalRequest.setStart(detail.getString("departure_station"));
                                    routeCalRequest.setEnd(detail.getString("arrive_station"));
                                    routeCalRequest.setTitle(detail.getString("name"));
                                    break;
                                }
                                case 3: {
                                    if (finalSteps.getString("instructions").contains("地铁")) {
                                        //按地铁处理
                                        routeCalRequest.setType(ConstCorrespond.PATH_INFO_TYPE[6]);
                                    } else {
                                        //按公交车处理
                                        routeCalRequest.setType(ConstCorrespond.PATH_INFO_TYPE[2]);
                                    }
                                    routeCalRequest.setDistance(finalSteps.getInteger("distance"));
                                    JSONObject detail = vehicle.getJSONObject("detail");
                                    routeCalRequest.setStart(detail.getString("on_station"));
                                    routeCalRequest.setEnd(detail.getString("off_station"));
                                    List<String> cities = new ArrayList<>();
                                    String path = finalSteps.getString("path");
                                    String[] points = path.split(";");
                                    double sumDistance = 0;
                                    for (int i = 0; i < points.length; i = i + 10) {
                                        String[] lngLat = points[i].split(",");
                                        String city = this.reverseGeoCoding(Double.parseDouble(lngLat[1]), Double.parseDouble(lngLat[0]));
                                        if (!cities.contains(city)) cities.add(city);
                                    }
                                    routeCalRequest.setCitys(cities);
                                    routeCalRequest.setTitle(detail.getString("name"));
                                    break;
                                }
                                default:
                                    continue;
                            }

                            routeCalRequest.setStartAdressZone(start);
                            routeInfo.getRouteCalRequestList().add(routeCalRequest);
                        }
                    }
                    routeInfoList.add(routeInfo);
                }
                return routeInfoList;
            } else {
                throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "百度api报错为" + jsonObject.getInteger("status"));
            }
            response.close();
            httpClient.close();
        }
        throw new AllException(EmAllException.BAIDU_REQUEST_FAILE, "配额已到上限");
    }
}
