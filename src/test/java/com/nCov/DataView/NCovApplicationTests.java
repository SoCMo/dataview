package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.request.RouteListRequest;
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
        int i = 1;
        for (RouteListRequest routeListRequest : baiduTool.pathInfo("昆明长水机场", "上海大学宝山校区").getPathList()) {
            System.out.println(i++);
            System.out.println(routeListRequest);
        }
    }

}
