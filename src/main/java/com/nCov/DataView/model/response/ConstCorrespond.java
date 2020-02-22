package com.nCov.DataView.model.response;

import lombok.Data;

/**
 * program: ConstCorrespond
 * description: 常量设置
 * author: SoCMo
 * create: 2019/12/14 12:13
 */
@Data
public class ConstCorrespond {
    public final static String[] APPOINTMENT_PROGRESS = {
            "",
            "未审核",
            "通过审核",
            "被驳回",
            "用户取消",
            "预约过期",
            "已评价"
    };
}
