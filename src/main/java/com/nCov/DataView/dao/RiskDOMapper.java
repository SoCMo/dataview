package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.RiskDO;
import com.nCov.DataView.model.entity.RiskDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiskDOMapper {
    int countByExample(RiskDOExample example);

    int deleteByExample(RiskDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RiskDO record);

    int insertSelective(RiskDO record);

    List<RiskDO> selectByExample(RiskDOExample example);

    RiskDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RiskDO record, @Param("example") RiskDOExample example);

    int updateByExample(@Param("record") RiskDO record, @Param("example") RiskDOExample example);

    int updateByPrimaryKeySelective(RiskDO record);

    int updateByPrimaryKey(RiskDO record);
}