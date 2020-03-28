package com.nCov.DataView.service;

import com.nCov.DataView.model.response.Result;

/**
 * program: StudentService
 * description: 学生信息
 * author: SoCMo
 * create: 2020/3/28
 */
public interface StudentService {
    /**
     * @Description: 获取学生信息
     * @Param: [minIndex, maxIndex]
     * @return: java.util.List<com.nCov.DataView.model.response.info.StudentInfoResponse>
     * @Author: SoCMo
     * @Date: 2020/3/28
     */
    public Result studentInfo(int minIndex, int maxIndex);
}
