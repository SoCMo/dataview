package com.nCov.DataView.tools;


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
    public static String doubleToString(Double number) {
        return String.format("%.2f", number * 100) + "%";
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
        double site = NumberTool.intDivision(rank, sum);
        int maxRank = 32;
        double[] score = {1, 2, 3, 4, 5, 6, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2};
        int number = 0;
        while (site - score[number] / 100 > 0) {
            site -= score[number++] / 100;
        }
        return number == maxRank ? 0 : 100 - (100 / maxRank) * number;
    }
}
