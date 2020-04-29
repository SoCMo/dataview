package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.BaiduTool;
import com.nCov.DataView.tools.GaoDeTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private EpidemicService epidemicService;

    @Resource
    private BaiduTool baiduTool;

    @Resource
    private GaoDeTool gaoDeTool;

    @Test
    void contextLoads() throws IOException, AllException {
        Map<String, Double> map = gaoDeTool.getCoding("上海大学宝山校区");
        System.out.println("lng: " + map.get("lng"));
        System.out.println("lat: " + map.get("lat"));

        Map<String, Double> map2 = baiduTool.geoCoding("上海大学宝山校区");
        System.out.println("\nlng: " + map2.get("lng"));
        System.out.println("lat: " + map2.get("lat"));
//        try {
//            baiduTool.pathInfo("中国上海上海市静安区长寿路999弄33号12A室", "上海大学宝山校区");
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//        }
    }

}
