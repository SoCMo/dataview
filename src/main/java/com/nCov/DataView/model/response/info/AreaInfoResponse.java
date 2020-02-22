package com.nCov.DataView.model.response.info;

import lombok.Data;

/**
 * program: AreaInfoResponse
 * description: 地区信息返回
 * author: SoCMo
 * create: 2020/2/21
 */
@Data
public class AreaInfoResponse {
    private String provincename;

    private String cityname;

    private Integer totalconfirm;

    private Integer totalsuspect;

    private Integer totaldead;

    private Integer totalheal;

    private Double cureRate;

    private Double mortality;

    private Double confirmInMillion;

    public Double cureRateCalculation() {
        if (this.totalconfirm == 0) return this.cureRate = 0.0;
        else
            return this.cureRate = Double.parseDouble(String.valueOf(this.totalheal)) / Double.parseDouble(String.valueOf(this.totalconfirm));
    }

    public Double mortalityCalculation() {
        if (this.totalconfirm == 0) return this.cureRate = 0.0;
        else
            return this.mortality = Double.parseDouble(String.valueOf(this.totaldead)) / Double.parseDouble(String.valueOf(this.totalconfirm));
    }

    public Double confirmCalculation(Integer population) {
        if (this.totalconfirm == 0) return this.cureRate = 0.0;
        else if (population == 0) return this.confirmInMillion = 0.0;
        else
            return this.confirmInMillion = Double.parseDouble(String.valueOf(this.totalconfirm)) / Double.parseDouble(String.valueOf(population)) * 1000000;
    }
}
