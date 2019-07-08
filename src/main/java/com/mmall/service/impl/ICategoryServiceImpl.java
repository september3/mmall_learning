package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * Created by sunlele
 * Date 2019/2/28 20:50
 * Description
 */

@Service("iCategoryService")
public class ICategoryServiceImpl implements ICategoryService {

    private static final Logger log = LoggerFactory.getLogger(ICategoryServiceImpl.class);

    @Resource
    CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName,Integer parentId){
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加品类参数错误");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        //这个分类是可用的
        category.setStatus(true);

        int rowCount = categoryMapper.insert(category);
        //判断插入数据库是否成功
        if(rowCount > 0){
            return ServerResponse.createBySuccess("添加品类成功");
        }
        return ServerResponse.createByErrorMessage("添加品类失败");
    }

    @Override
    public ServerResponse updateCategory(Integer categoryId,String categoryName){
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新品类参数错误");
        }

        Category category = new Category();
        //数据更新
        category.setId(categoryId);
        category.setName(categoryName);

        //通过主键进行更新
        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(rowCount > 0){
            return ServerResponse.createBySuccessMessage("更新品类名字成功");
        }
        return ServerResponse.createByErrorMessage("更新品类名字失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildCategory(Integer categoryId){
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        if(CollectionUtils.isEmpty(categoryList)){
            log.info("未找到当前分类的子目录");
            return ServerResponse.createByErrorMessage("未找到当前分类的子目录");
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId categoryId
     * @return ServerResponse<List<Integer>>
     */
    @Override
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);

        List<Integer> catagoryIdList  = Lists.newArrayList();
        if(categoryId != null){
            for (Category categoryItem : categorySet){
                catagoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(catagoryIdList);
    }

    /**
     * 递归算法，算出子节点
     * @param categorySet categorySet
     * @param categoryId categoryId
     * @return Set<Category>
     */
    private Set<Category> findChildCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            //将从数据库中查到的category放到set集合中
            categorySet.add(category);
        }
        //查找子节点，递归算法的退出条件
        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryId);
        }
        return categorySet;
    }




}
