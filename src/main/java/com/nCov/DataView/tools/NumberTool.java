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
}
