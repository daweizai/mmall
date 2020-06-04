package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

public interface IUserService {

    /**
     * 用户登录
     */
    ServerResponse<User> login(String username, String password);

    /**
     * 添加用户信息
     * @param user 用户对象信息
     */
    ServerResponse<String> addUser(User user);

    /**
     * 验证用户是否存在
     * @param value 需要验证的值
     * @param type 要检验的类型  email、username
     */
    ServerResponse<String> checkValid(String value, String type);

    /**
     * 查询找回密码问题
     * @param username 用户名
     */
    ServerResponse<String> selectQuestion(String username);

    /**
     * 检查用户问题的答案是否正确
     * @param username 用户名
     * @param question 问题
     * @param answer 用户输入的问题答案
     */
    ServerResponse<String> checkAnswer(String username, String question, String answer);
}
