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
        System.out.println(baiduTool.pathInfo("中国辽宁省大连市", "上海大学宝山校区"));
    }

}
