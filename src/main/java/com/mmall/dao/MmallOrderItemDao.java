package com.mmall.dao;

import com.mmall.pojo.MmallOrderItem;

public interface MmallOrderItemDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallOrderItem record);

    int insertSelective(MmallOrderItem record);

    MmallOrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallOrderItem record);

    int updateByPrimaryKey(MmallOrderItem record);
}