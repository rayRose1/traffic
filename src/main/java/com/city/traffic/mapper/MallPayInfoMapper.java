package com.city.traffic.mapper;

import com.city.traffic.entity.MallPayInfo;
import com.city.traffic.entity.MallPayInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ray
 * @version 1.0
 * @date 2022/4/18 9:45
 */
@Mapper
public interface MallPayInfoMapper {
    long countByExample(MallPayInfoExample example);

    int deleteByExample(MallPayInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(MallPayInfo record);

    int insertSelective(MallPayInfo record);

    List<MallPayInfo> selectByExample(MallPayInfoExample example);

    MallPayInfo selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") MallPayInfo record, @Param("example") MallPayInfoExample example);

    int updateByExample(@Param("record") MallPayInfo record, @Param("example") MallPayInfoExample example);

    int updateByPrimaryKeySelective(MallPayInfo record);

    int updateByPrimaryKey(MallPayInfo record);
}
