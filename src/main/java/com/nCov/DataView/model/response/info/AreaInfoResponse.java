package com.nCov.DataView.model.response.info;

import com.nCov.DataView.tools.NumberTool;
import lombok.Data;

/**
 * program: AreaInfoResponse
 * description: 地区信息返回
 * author: SoCMo
 * create: 2020/2/21
 */
@Data
public class AreaInfoResponse implements Comparable {
    private String provincename;

    private String cityname;

    private Integer totalconfirm;

    private Integer totalsuspect;

    private Integer totaldead;

    private Integer totalheal;

    private static Integer order;
    private static Integer isUp;
    private String cureRate;
    private String mortality;
    private String confirmInMillion;

    /**
     * @Description: 初始化排序变量
     * @Param: []
     * @return: void
     * @Author: SoCMo
     * @Date: 2020/2/23
     */
    public static void init(Integer isUpIN, Integer orderIN) {
        isUp = isUpIN;
        order = orderIN;
    }

    public String cureRateCalculation() {
        if (this.totalconfirm == 0) {
            return this.cureRate = "0%";
        }
        return this.cureRate = NumberTool.doubleToString(NumberTool.intDivision(this.totalheal, this.totalconfirm));
    }

    public String mortalityCalculation() {
        if (this.totalconfirm == 0) return this.cureRate = "0%";
        return this.mortality = NumberTool.doubleToString(NumberTool.intDivision(this.totaldead, this.totalconfirm));
    }

    public String confirmCalculation(Integer population) {
        if (this.totalconfirm == 0) return this.confirmInMillion = "0";
        else if (population == 0) return this.confirmInMillion = "该地区暂无人口数据";
        else
            return this.confirmInMillion = NumberTool.doubleToString(NumberTool.intDivision(this.totalconfirm, population) * 1000000);
    }

    /**
     * @Description: 排序方法
     * @Param: [o]
     * @return: int
     * @Author: SoCMo
     * @Date: 2020/2/23
     */
    @Override
    public int compareTo(Object o) {
        AreaInfoResponse a = (AreaInfoResponse) o;
        if (a == null) {
            return 1;
        }
        switch (order) {
            case 0:
                if (this.totalconfirm > a.totalconfirm) return isUp;
                else if (this.totalconfirm.equals(a.totalconfirm)) return 0;
                else return -1 * isUp;
            case 1:
                if (this.totalheal > a.totalheal) return isUp;
                else if (this.totalheal.equals(a.totalheal)) return 0;
                else return -1 * isUp;
            case 2:
                if (this.totaldead > a.totaldead) return isUp;
                else if (this.totaldead.equals(a.totaldead)) return 0;
                else return -1 * isUp;
            case 3:
                if (this.cureRate.compareTo(a.cureRate) > 0) return isUp;
                else if (this.cureRate.equals(a.cureRate)) return 0;
                else return -1 * isUp;
            case 4:
                if (this.mortality.compareTo(a.mortality) > 0) return isUp;
                else if (this.mortality.equals(a.mortality)) return 0;
                else return -1 * isUp;
            case 5:
                if (this.confirmInMillion.equals(a.confirmInMillion)) return 0;
                else if (this.confirmInMillion.equals("该地区暂无人口数据")) return -1;
                else if (a.confirmInMillion.equals("该地区暂无人口数据")) return 1;
                else if (this.confirmInMillion.compareTo(a.confirmInMillion) > 0) return isUp;
                else if (this.confirmInMillion.compareTo(a.confirmInMillion) < 0) return -1 * isUp;
        }
        return 0;
    }
}
