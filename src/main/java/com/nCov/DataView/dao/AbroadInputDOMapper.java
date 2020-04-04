package com.nCov.DataView.dao;

import com.nCov.DataView.model.entity.AbroadInputDO;
import com.nCov.DataView.model.entity.AbroadInputDOExample;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface AbroadInputDOMapper {
    int countByExample(AbroadInputDOExample example);

    int deleteByExample(AbroadInputDOExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AbroadInputDO record);

    int insertSelective(AbroadInputDO record);

    List<AbroadInputDO> selectByExample(AbroadInputDOExample example);

    AbroadInputDO selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AbroadInputDO record, @Param("example") AbroadInputDOExample example);

    int updateByExample(@Param("record") AbroadInputDO record, @Param("example") AbroadInputDOExample example);

    int updateByPrimaryKeySelective(AbroadInputDO record);

    int updateByPrimaryKey(AbroadInputDO record);

    @MapKey("provincename")
    @Select({"<script>",
            "SELECT * FROM abroadInput",
            "WHERE date(date) = '${date}'",
            "</script>"})
    Map<String, AbroadInputDO> getInfoByDate(@Param("date") String date);

}