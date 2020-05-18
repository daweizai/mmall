package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;

public interface IUserService {

    /**
     * 用户登录
     */
    ServerResponse<MmallUser> login(String username, String password);
}
