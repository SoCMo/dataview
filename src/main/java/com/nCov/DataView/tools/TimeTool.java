package com.nCov.DataView.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * program: TimeTool
 * description: 时间转换
 * author: SoCMo
 * create: 2019/12/6 14:28
 */
public class TimeTool {
    /**
     * @Description: date转换为最小为分的格式
     * @Param: [date]
     * @Return: java.lang.String
     * @Author: SoCMo
     * @Date: 2019/12/6
     */
    public static String timeToMinute(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        //设置时区为Asia/Shanghai
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * @Description: date转换为最小为天的格式
     * @Param: [date]
     * @Return: java.lang.String
     * @Author: SoCMo
     * @Date: 2019/12/6
     */
    public static String timeToDayCN(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        //设置时区为Asia/Shanghai
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * @Description: date转换为最小为天的格式2
     * @Param: [date]
     * @Return: java.lang.String
     * @Author: SoCMo
     * @Date: 2019/12/11
     */
    public static String timeToDaySy(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //设置时区为Asia/Shanghai
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return simpleDateFormat.format(date);
    }

    /**
     * @Description: 字符串转化为Date类型
     * @Param: [time]
     * @Return: java.util.Date
     * @Author: SoCMo
     * @Date: 2019/12/11
     */
    public static Date stringToDay(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(time);
    }

    /**
     * @Description: 计算字符串类型的日期差
     * @Param: [begin, end]
     * @return: int
     * @Author: SoCMo
     * @Date: 2020/2/26
     */
    public static int dayDiffStr(String begin, String end) throws ParseException {
        long from = TimeTool.stringToDay(begin).getTime();
        long to = TimeTool.stringToDay(end).getTime();
        return (int) ((to - from) / (1000 * 60 * 60 * 24));
    }

    /**
     * @Description: 计算Date类型日期差
     * @Param: [begin, end]
     * @return: int
     * @Author: SoCMo
     * @Date: 2020/2/26
     */
    public static int dayDiffDate(Date begin, Date end) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return TimeTool.dayDiffStr(dateFormat.format(begin), dateFormat.format(end));
    }
}
