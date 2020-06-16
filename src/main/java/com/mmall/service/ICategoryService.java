package com.mmall.service;


import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {


    /**
     * 添加分类
     * @param categoryName
     * @param parentId
     * @return
     */
    ServerResponse<String> addCategory(String categoryName, Integer parentId);

    /**
     * 更新分类
     * @param categoryName
     * @param categoryId
     * @return
     */
    ServerResponse<String> updateCategory(String categoryName, Integer categoryId);

    /**
     * 查询当前categoryId的子类
     * @param categoryId
     * @return
     */
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);


    /**
     * 递归查询本节点的id以及孩子节点的id
     * @param categoryId
     * @return
     */
    ServerResponse selectCategoryAndChildrenById(Integer categoryId);
}
