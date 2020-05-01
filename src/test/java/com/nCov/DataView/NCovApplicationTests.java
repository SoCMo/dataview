package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.response.info.SiteInfo;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.BaiduTool;
import com.nCov.DataView.tools.GaoDeTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

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
        System.out.println(gaoDeTool.routePlanning("杭州市杭州东站", "上海市虹桥火车站"));
//        SiteInfo siteInfo = gaoDeTool.getSitesList("宁波市鼓楼站", "宁波市长江路站(A2口)",
//                "轨道交通1号线", 1);
//        System.out.println(siteInfo);
//        try {
//            baiduTool.pathInfo("中国上海上海市静安区长寿路999弄33号12A室", "上海大学宝山校区");
//        } catch (Exception e) {
//            System.out.println(e.getLocalizedMessage());
//        }
    }

}
