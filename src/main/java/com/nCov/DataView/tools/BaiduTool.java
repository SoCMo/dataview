package com.nCov.DataView.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.exception.EmAllException;
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
                return jsonObject.getJSONObject("result")
                        .getJSONObject("addressComponent")
                        .getString("city");
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


}
