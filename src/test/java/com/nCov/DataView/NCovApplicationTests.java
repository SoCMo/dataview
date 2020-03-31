package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.tools.BaiduTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private BaiduTool baiduTool;

    @Test
    void contextLoads() throws IOException, AllException {
        //baiduTool.pathInfo("浙江省台州市天台县汇泉东街5-7", "上海大学宝山校区");
        Map<String, Double> map = baiduTool.geoCoding("浙江省台州市天台县汇泉东街5-7");

        System.out.println(map);
    }

}
