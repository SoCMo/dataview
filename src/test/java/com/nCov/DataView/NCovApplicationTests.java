package com.nCov.DataView;

import com.nCov.DataView.dao.CovDataMapper;
import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.tools.TimeTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private CovDataMapper covDataMapper;

    @Test
    void contextLoads() throws ParseException {
        CovData covData = new CovData();
        covData.setDate(TimeTool.stringToDay("2020-3-23"));
        covData.setTotaldead(0);
        covData.setTotalconfirm(0);
        covData.setTotalsuspect(0);
        covData.setTotalheal(0);
        covData.setProvincename("Test");
        covData.setAreaname("Test");
        covData.setIsprovince(1);
        List<CovData> covDataList = new ArrayList<>();
        covDataList.add(covData);
        covDataMapper.insertList(covDataList);
    }

}
