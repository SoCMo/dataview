package com.nCov.DataView.model.entity;

import lombok.Data;

/**
 * @Description: 创建学生住址和回校方式请求体
 * @Author: pongshy
 * @Date: 2020/3/28
 */
@Data
public class StudentInfo {
    private String province;

    private String city;

    private String country;

    private String area;

    private String address;

    private String transportion;
}
