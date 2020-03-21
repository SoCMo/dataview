package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.CovData;
import com.nCov.DataView.model.entity.CovDataExample;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface CovDataMapper {
    int countByExample(CovDataExample example);

    int deleteByExample(CovDataExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(CovData record);

    int insertSelective(CovData record);

    List<CovData> selectByExample(CovDataExample example);

    CovData selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") CovData record, @Param("example") CovDataExample example);

    int updateByExample(@Param("record") CovData record, @Param("example") CovDataExample example);

    int updateByPrimaryKeySelective(CovData record);

    int updateByPrimaryKey(CovData record);

    @MapKey("areaname")
    @Select({"<script>",
            "SELECT * FROM dataFromTencent_dev",
            "WHERE date(date) = '${date}' AND isprovince = 0",
            "GROUP BY id",
            "</script>"})
    Map<String, CovData> getInfoByDate(@Param("date") String date);

    @Select({"<script>",
            "SELECT * FROM dataFromTencent_dev",
            "WHERE provinceName = areaName",
            "Order By date ASC",
            "</script>"})
    List<CovData> getInfoProvince();

    void insertList(List<CovData> covDataList);
}