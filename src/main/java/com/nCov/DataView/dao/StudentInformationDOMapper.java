package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.StudentInformationDO;
import com.nCov.DataView.model.entity.StudentInformationDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface StudentInformationDOMapper {
    int countByExample(StudentInformationDOExample example);

    int deleteByExample(StudentInformationDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StudentInformationDO record);

    int insertSelective(StudentInformationDO record);

    List<StudentInformationDO> selectByExample(StudentInformationDOExample example);

    StudentInformationDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StudentInformationDO record, @Param("example") StudentInformationDOExample example);

    int updateByExample(@Param("record") StudentInformationDO record, @Param("example") StudentInformationDOExample example);

    int updateByPrimaryKeySelective(StudentInformationDO record);

    int updateByPrimaryKey(StudentInformationDO record);
}