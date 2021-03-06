package com.mmall.dao;

import com.mmall.pojo.MmallUser;
import org.apache.ibatis.annotations.Param;

public interface MmallUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(MmallUser record);

    int insertSelective(MmallUser record);

    MmallUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MmallUser record);

    int updateByPrimaryKey(MmallUser record);

    int checkUsername(String username);

    MmallUser selectByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}