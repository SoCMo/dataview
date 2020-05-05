package com.nCov.DataView.model.response.info;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * program: myRisk
 * description: 我的风险指数
 * author: SoCMo
 * create: 2020/5/5
 */
@Data
@NoArgsConstructor
public class MyRisk {
    private List<DateCal> dateCalList;

    private String todayRisk;
}
