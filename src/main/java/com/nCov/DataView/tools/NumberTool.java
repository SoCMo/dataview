package com.nCov.DataView.tools;


import com.nCov.DataView.model.ConstCorrespond;
import com.nCov.DataView.model.request.RouteCalRequest;
import com.nCov.DataView.model.response.info.RouteCalReponse;

/**
 * program: NumberTool
 * description: 数字转换
 * author: SoCMo
 * create: 2020/2/23
 */
public class NumberTool {

    /**
     * @Description: 浮点数保留两位小数转换为百分数
     * @Param: [number]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/2/23
     */
    public static String doubleToStringWithH(Double number) {
        return String.format("%.2f", number * 100) + "%";
    }

    /**
     * @Description: 浮点数保留两位小数
     * @Param: [number]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/2/23
     */
    public static String doubleToStringWotH(Double number) {
        return String.format("%.2f", number);
    }

    /**
     * @Description: 整数除法转浮点数
     * @Param: [a, b]
     * @return: java.lang.String
     * @Author: SoCMo
     * @Date: 2020/2/23
     */
    public static Double intDivision(Integer a, Integer b) {
        return (double) a / (double) b;
    }

    /**
     * @Description: 排名赋分
     * @Param: [rank, sum]
     * @return: int
     * @Author: SoCMo
     * @Date: 2020/3/17
     */
    public static int Score(Integer rank, Integer sum) {
        int site = (int) (NumberTool.intDivision(rank, sum) * 100 + 0.99);
        int[] score = {1, 2, 3, 4, 10, 10, 10, 10, 10, 10, 10, 10, 4, 3, 2, 1};
        int number = 0;
        while (site - score[number] > 0) {
            site -= score[number++];
        }

        return number == score.length - 1 ? 0 : 100 - (100 / score.length) * number;
    }

    /**
     * @Description: 计算得分
     * @Param: [routeCalRequest]
     * @return: double
     * @Author: SoCMo
     * @Date: 2020/3/29
     */
    public static void routeCal(RouteCalReponse routeCalReponse, RouteCalRequest routeCalRequest, double time) {
        //TODO 计算公式
        routeCalReponse.setFinalscore(
                (ConstCorrespond.ROUTE_WEIGHT[0] * ConstCorrespond.CROWD[routeCalReponse.getType()]
                        + ConstCorrespond.ROUTE_WEIGHT[1] * (time >= 24 ? 100 : (time / 24.0 * 100))
                        + ConstCorrespond.ROUTE_WEIGHT[2] * ConstCorrespond.CLEAN_SCORE[routeCalReponse.getType()]
                        + ConstCorrespond.ROUTE_WEIGHT[3] * 0) / 20.0
        );
        return;
    }
}
