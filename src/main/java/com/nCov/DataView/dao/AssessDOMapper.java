package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.AssessDO;
import com.nCov.DataView.model.entity.AssessDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AssessDOMapper {
    int countByExample(AssessDOExample example);

    int deleteByExample(AssessDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AssessDO record);

    int insertSelective(AssessDO record);

    List<AssessDO> selectByExample(AssessDOExample example);

    AssessDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AssessDO record, @Param("example") AssessDOExample example);

    int updateByExample(@Param("record") AssessDO record, @Param("example") AssessDOExample example);

    int updateByPrimaryKeySelective(AssessDO record);

    int updateByPrimaryKey(AssessDO record);

    List<AssessDO> selectMax(@Param("date") String date, @Param("index") int index, @Param("num") int num, @Param("province") String province);

    void insertList(List<AssessDO> assessDOList);
}