package com.nCov.DataView;

import com.nCov.DataView.tools.BaiduTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NCovApplicationTests {
//    @Resource
//    private EpidemicService epidemicService;

    @Resource
    private BaiduTool baiduTool;

    @Test
    void contextLoads() {
//        epidemicService.assess();
        try {
            baiduTool.pathInfo("中国上海上海市静安区长寿路999弄33号12A室", "上海大学宝山校区");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
