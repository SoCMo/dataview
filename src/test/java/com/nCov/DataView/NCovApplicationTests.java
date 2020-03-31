package com.nCov.DataView;

import com.nCov.DataView.service.EpidemicService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    EpidemicService epidemicService;

    @Test
    void contextLoads() throws ParseException {
        epidemicService.getAllRouteCal(1, 10);
    }

}
