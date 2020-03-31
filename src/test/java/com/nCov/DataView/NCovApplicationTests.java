package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.tools.BaiduTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private BaiduTool baiduTool;

    @Test
    void contextLoads() throws IOException, AllException {
//        RouteListRequest routeListRequest = baiduTool.pathInfo("昆明市官渡区五金机电精品市场c-6", "上海大学宝山校区", 1);
//        System.out.println(routeListRequest.getRouteCalRequestList());

        baiduTool.reverseGeoCoding(31.197603576031, 121.35369928996);
    }

}
