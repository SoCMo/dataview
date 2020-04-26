package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.StatisticDO;
import com.nCov.DataView.model.entity.StatisticDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatisticDOMapper {
    int countByExample(StatisticDOExample example);

    int deleteByExample(StatisticDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(StatisticDO record);

    int insertSelective(StatisticDO record);

    List<StatisticDO> selectByExample(StatisticDOExample example);

    StatisticDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") StatisticDO record, @Param("example") StatisticDOExample example);

    int updateByExample(@Param("record") StatisticDO record, @Param("example") StatisticDOExample example);

    int updateByPrimaryKeySelective(StatisticDO record);

    int updateByPrimaryKey(StatisticDO record);
}