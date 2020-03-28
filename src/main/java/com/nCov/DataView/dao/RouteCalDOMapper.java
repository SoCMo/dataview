package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.RouteCalDO;
import com.nCov.DataView.model.entity.RouteCalDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RouteCalDOMapper {
    int countByExample(RouteCalDOExample example);

    int deleteByExample(RouteCalDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RouteCalDO record);

    int insertSelective(RouteCalDO record);

    List<RouteCalDO> selectByExample(RouteCalDOExample example);

    RouteCalDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RouteCalDO record, @Param("example") RouteCalDOExample example);

    int updateByExample(@Param("record") RouteCalDO record, @Param("example") RouteCalDOExample example);

    int updateByPrimaryKeySelective(RouteCalDO record);

    int updateByPrimaryKey(RouteCalDO record);

    void insertList(List<RouteCalDO> routeCalDOList);
}