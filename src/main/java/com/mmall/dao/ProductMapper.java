package com.mmall.dao;

import com.mmall.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 查询商品列表
     * @return productList
     */
    List<Product> selectList();

    /**
     * 通过商品名称和ID进行产品搜索
     * @param productName productName
     * @param productId productId
     * @return
     */
    List<Product> selectByNameAndProductId(@Param("productName")String productName,@Param("productId")Integer productId);

    List<Product> selectByNameAndCategoryIds(@Param("name") String name,@Param("categoryIdList") List<Integer> categoryIdList);
}