package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.PassInfoDO;
import com.nCov.DataView.model.entity.PassInfoDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PassInfoDOMapper {
    int countByExample(PassInfoDOExample example);

    int deleteByExample(PassInfoDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PassInfoDO record);

    int insertSelective(PassInfoDO record);

    List<PassInfoDO> selectByExample(PassInfoDOExample example);

    PassInfoDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PassInfoDO record, @Param("example") PassInfoDOExample example);

    int updateByExample(@Param("record") PassInfoDO record, @Param("example") PassInfoDOExample example);

    int updateByPrimaryKeySelective(PassInfoDO record);

    int updateByPrimaryKey(PassInfoDO record);
}