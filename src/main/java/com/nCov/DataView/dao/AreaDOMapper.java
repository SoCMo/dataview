package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.AreaDO;
import com.nCov.DataView.model.entity.AreaDOExample;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AreaDOMapper {
    int countByExample(AreaDOExample example);

    int deleteByExample(AreaDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AreaDO record);

    int insertSelective(AreaDO record);

    List<AreaDO> selectByExample(AreaDOExample example);

    AreaDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AreaDO record, @Param("example") AreaDOExample example);

    int updateByExample(@Param("record") AreaDO record, @Param("example") AreaDOExample example);

    int updateByPrimaryKeySelective(AreaDO record);

    int updateByPrimaryKey(AreaDO record);

    @Select({"<script>",
            "SELECT * FROM area",
            "WHERE name like '${cityName}'",
            "</script>"})
    List<AreaDO> mapSelectByName(@Param("cityName") String cityName);

    @MapKey("name")
    @Select({"<script>",
            "SELECT * FROM area",
            "WHERE parentId <![CDATA[<]]> 35 and parentId != 0",
            "GROUP BY id",
            "</script>"})
    Map<String, AreaDO> getCityMap();

    @MapKey("id")
    @Select({"<script>",
            "SELECT * FROM area",
            "WHERE parentId = 0",
            "GROUP BY id",
            "</script>"})
    Map<Integer, AreaDO> getProvinceMap();

    @Select({"<script>",
            "SELECT * FROM area",
            "WHERE name LIKE '${name}%'",
            "</script>"})
    AreaDO nameLike(@Param("name") String name);
}