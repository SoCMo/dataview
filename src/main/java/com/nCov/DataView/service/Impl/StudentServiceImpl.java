package com.nCov.DataView.service.Impl;


import com.nCov.DataView.dao.StudentInformationDOMapper;
import com.nCov.DataView.model.entity.StudentInformationDO;
import com.nCov.DataView.model.entity.StudentInformationDOExample;
import com.nCov.DataView.model.response.Result;
import com.nCov.DataView.model.response.info.StudentInfoResponse;
import com.nCov.DataView.service.StudentService;
import com.nCov.DataView.tools.ResultTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * program: StudentServiceImpl
 * description: 学生信息业务层
 * author: SoCMo
 * create: 2020/3/28
 */
@Slf4j
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    StudentInformationDOMapper studentInformationDOMapper;

    /**
     * @Description: 学生信息请求
     * @Param: [minIndex, maxIndex]
     * @return: java.util.List<com.nCov.DataView.model.response.info.StudentInfoResponse>
     * @Author: SoCMo
     * @Date: 2020/3/28
     */
    public Result studentInfo(int index, int max) {
        StudentInformationDOExample studentInformationDOExample = new StudentInformationDOExample();
        studentInformationDOExample.setOrderByClause("id limit " + index + "," + max);
        List<StudentInformationDO> studentInformationDOList = studentInformationDOMapper.selectByExample(studentInformationDOExample);
        return ResultTool.success(studentInformationDOList.stream().map(studentInformationDO -> {
            StudentInfoResponse studentInfoResponse = new StudentInfoResponse();
            BeanUtils.copyProperties(studentInformationDO, studentInfoResponse);
            studentInfoResponse.setTransportation(studentInformationDO.getTransportion());
            return studentInfoResponse;
        }).collect(Collectors.toList()));
    }
}
