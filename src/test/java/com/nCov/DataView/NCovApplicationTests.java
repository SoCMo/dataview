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
        baiduTool.pathInfo("浙江省杭州市临安区碧桂园", "上海大学宝山校区");
    }

}
