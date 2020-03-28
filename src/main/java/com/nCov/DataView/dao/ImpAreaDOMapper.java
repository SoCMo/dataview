package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.ImpAreaDO;
import com.nCov.DataView.model.entity.ImpAreaDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ImpAreaDOMapper {
    int countByExample(ImpAreaDOExample example);

    int deleteByExample(ImpAreaDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ImpAreaDO record);

    int insertSelective(ImpAreaDO record);

    List<ImpAreaDO> selectByExample(ImpAreaDOExample example);

    ImpAreaDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") ImpAreaDO record, @Param("example") ImpAreaDOExample example);

    int updateByExample(@Param("record") ImpAreaDO record, @Param("example") ImpAreaDOExample example);

    int updateByPrimaryKeySelective(ImpAreaDO record);

    int updateByPrimaryKey(ImpAreaDO record);

    void insertList(List<ImpAreaDO> impAreaDOList);
}