package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.PathInfoDO;
import com.nCov.DataView.model.entity.PathInfoDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PathInfoDOMapper {
    int countByExample(PathInfoDOExample example);

    int deleteByExample(PathInfoDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PathInfoDO record);

    int insertSelective(PathInfoDO record);

    List<PathInfoDO> selectByExample(PathInfoDOExample example);

    PathInfoDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PathInfoDO record, @Param("example") PathInfoDOExample example);

    int updateByExample(@Param("record") PathInfoDO record, @Param("example") PathInfoDOExample example);

    int updateByPrimaryKeySelective(PathInfoDO record);

    int updateByPrimaryKey(PathInfoDO record);
}