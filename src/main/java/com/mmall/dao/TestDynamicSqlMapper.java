package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

/**
 * Created by sunlele
 * Date 2019/3/4 21:17
 * Description
 */
public interface TestDynamicSqlMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(@Param("record") Cart record);

    int insertSelective(Cart record);

//    Cart selectByPrimaryKey(Integer id);
//
//    int updateByPrimaryKeySelective(Cart record);
//
//    int updateByPrimaryKey(Cart record);

}
