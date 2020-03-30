package com.nCov.DataView;

import com.nCov.DataView.tools.FixTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private FixTool fixTool;

    @Test
    void contextLoads() throws ParseException {
        fixTool.dataFix();
    }

}
