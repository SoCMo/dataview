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
        int site = (int) (NumberTool.intDivision(rank, sum) * 100 + 0.99);
        int[] score = {1, 2, 3, 4, 10, 10, 10, 10, 10, 10, 10, 10, 4, 3, 2, 1};
        int number = 0;
        while (site - score[number] > 0) {
            site -= score[number++];
        }

        return number == score.length - 1 ? 0 : 100 - (100 / score.length) * number;
    }
}
