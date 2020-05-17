package com.mmall.dao;

import com.mmall.pojo.MmallProduct;

public interface MmallProductDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallProduct record);

    int insertSelective(MmallProduct record);

    MmallProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallProduct record);

    int updateByPrimaryKey(MmallProduct record);
}