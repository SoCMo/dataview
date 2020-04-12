package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.AssessDO;
import com.nCov.DataView.model.entity.AssessDOExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
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


    @Select({"<script>",
            "SELECT * FROM assess",
            "WHERE date(update_time) = '${date}'",
            "AND area_name LIKE '中国${province}%'",
            "GROUP BY path_id",
            "ORDER BY sum_score DESC",
            "limit index ${index}, ${num}",
            "</script>"})
    List<AssessDO> selectPathId(Date date, int index, int num, String province);

    void insertList(List<AssessDO> assessDOList);
}