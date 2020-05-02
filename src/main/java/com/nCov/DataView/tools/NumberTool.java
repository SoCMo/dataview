package com.nCov.DataView.tools;


/**
 * program: NumberTool
 * description: 数字转换
 * author: SoCMo
 * create: 2020/2/23
 */
public class NumberTool {
    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

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
        return String.format("%.1f", number);
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
        int[] score = {5, 6, 7, 7, 7, 8, 10, 10, 10, 10, 10, 4, 3, 2, 1};
        int number = 0;
        while (site - score[number] > 0) {
            site -= score[number++];
        }

        return number == score.length - 1 ? 0 : 100 - (100 / score.length) * number;
    }

    /**
     * @Description: 经纬度计算距离
     * @Param: [lat1, lng1, lat2, lng2]
     * @return: double
     * @Author: SoCMo
     * @Date: 2020/3/30
     */
    public static double getDistance(double lat1, double lng1, double lat2,
                                     double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));

        //地球半径
        double EARTH_RADIUS = 6378.137;
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        s = s * 1000;
        return s;
    }
}
