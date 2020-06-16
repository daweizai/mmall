package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryDao;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @Author daweizai
 * @Date 21:36 2020/6/12
 * @ClassName CategoryServiceImpl
 * @Version 1.0
 **/
@Service("iCategoryService")
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryDao categoryDao;


    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.error("添加品类参数失败");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int rowCount = categoryDao.insert(category);
        if (rowCount > 0) {
            return ServerResponse.successByMsg("添加品类成功");
        }
        return ServerResponse.error("添加品类失败");

    }

    @Override
    public ServerResponse<String> updateCategory(String categoryName, Integer categoryId) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.error("更新品类参数失败");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);
        int rowCount = categoryDao.updateByPrimaryKeySelective(category);
        if (rowCount > 0) {
            return ServerResponse.successByMsg("更新品类成功");
        }
        return ServerResponse.error("更新品类失败");
    }


    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categoryList = categoryDao.selectCategoryChildrenByParentId(categoryId);
        if (categoryList.isEmpty()) {
            log.info("未找到当前分类的子分类");
        }
        return ServerResponse.success(categoryList);
    }


    @Override
    public ServerResponse selectCategoryAndChildrenById(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet, categoryId);

        List<Integer> caIntegerList = Lists.newArrayList();
        if (categoryId != null) {
            for (Category categoryItem : categorySet) {
                caIntegerList.add(categoryItem.getId());
            }
        }
        return ServerResponse.success(caIntegerList);
    }

    private Set<Category> findChildCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryDao.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }
        //查询子节点，递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryDao.selectCategoryChildrenByParentId(categoryId);
        for (Category category1 : categoryList) {
            findChildCategory(categorySet, category1.getId());
        }
        return categorySet;
    }
}
