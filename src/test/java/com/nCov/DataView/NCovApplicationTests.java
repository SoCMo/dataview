package com.nCov.DataView;

import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.service.EpidemicService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private EpidemicService epidemicService;

    @Test
    void contextLoads() throws AllException, ParseException {
        epidemicService.assess();
    }

}
