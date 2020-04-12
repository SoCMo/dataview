package com.nCov.DataView;

import com.nCov.DataView.tools.FixTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class NCovApplicationTests {

    @Resource
    private FixTool fixTool;

    @Test
    void contextLoads() {
        try {
            fixTool.LngAndLatFix();
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

}
