package com.nCov.DataView;

import com.nCov.DataView.dao.PathInfoDOMapper;
import com.nCov.DataView.exception.AllException;
import com.nCov.DataView.model.request.PathRequest;
import com.nCov.DataView.model.request.RouteListRequest;
import com.nCov.DataView.service.EpidemicService;
import com.nCov.DataView.tools.BaiduTool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class NCovApplicationTests {
    @Resource
    private BaiduTool baiduTool;

    @Resource
    private EpidemicService epidemicService;

    @Test
    void contextLoads() throws IOException, AllException {
//        int i = 1;
//        PathRequest pathRequest = baiduTool.pathInfo("中国浙江省湖州市吴兴区余家漾月漾苑", "上海大学宝山校区");
//        for (RouteListRequest routeListRequest : pathRequest.getPathList()) {
//            System.out.println(i++);
//            System.out.println(routeListRequest);
//        }
//        System.out.println(epidemicService.getSpecifiedNumber(1, 10));
        StringBuilder str = new StringBuilder();
        String demo = "[];'][]九亭镇九亭大街471-39-60  ";
        String regex = "[a-zA-Z0-9\\u4e00-\\u9fa5-]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(demo);

        while (matcher.find()) {
            str.append(matcher.group());
        }
        System.out.println(str.toString());
    }

}
