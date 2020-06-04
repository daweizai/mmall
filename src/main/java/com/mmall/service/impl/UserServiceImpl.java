package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserDao;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service(value = "iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserDao userDao;

    @Override
    public ServerResponse<User> login(String username, String password) {
        //查询username的用户是否存在
        int resultCount = userDao.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.error("用户不存在");
        }
        // md5密码加密
        password = MD5Util.MD5EncodeUtf8(password);
        User user = userDao.selectByUsernameAndPassword(username, password);
        if (user == null) {
            return ServerResponse.error("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success(user, "登录成功");
    }


    @Override
    public ServerResponse<String> addUser(User user) {
        //校验用户名
        ServerResponse<String> checkUsername = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!checkUsername.isSuccess()) {
            return checkUsername;
        }
        //校验邮箱
        ServerResponse<String> checkEmail = this.checkValid(user.getUsername(), Const.EMAIL);
        if (!checkEmail.isSuccess()) {
            return checkEmail;
        }
        //设置用户角色,默认设置为普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        //给密码添加MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        int insert = userDao.insert(user);
        if (insert == 0) {
            return ServerResponse.error("注册失败");
        }
        return ServerResponse.successByMsg("注册成功");
    }

    @Override
    public ServerResponse<String> checkValid(String value, String type) {
        if (StringUtils.isNotBlank(type)) {
            if (Const.EMAIL.equals(type)) {
                //校验邮箱
                int checkEmail = userDao.checkEmail(value);
                if (checkEmail > 0) {
                    return ServerResponse.error("该邮箱已被注册");
                }
            }
            if (Const.USERNAME.equals(type)) {
                //校验用户名
                int checkUsername = userDao.checkUsername(value);
                if (checkUsername > 0) {
                    return ServerResponse.error("用户名已存在");
                }
            }
        } else {
            return ServerResponse.error("参数错误");
        }
        return ServerResponse.successByMsg("校验通过");
    }


    @Override
    public ServerResponse<String> selectQuestion(String username) {
        //查询用户是否存在
        ServerResponse<String> checkValid = this.checkValid(username, Const.USERNAME);
        if (checkValid.isSuccess()) {
            return ServerResponse.error("用户不存在");
        }
        //存在，则通过用户名查询问题
        String question = userDao.selectQuestionByUsername(username);
        if (StringUtils.isNotBlank(question)) {
            return ServerResponse.success(question);
        }
        return ServerResponse.error("用户未设置找回密码问题，无法使用该方法找回密码。");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int checkAnswer = userDao.checkAnswer(username, question, answer);
        if (checkAnswer > 0) {
            //说明问题以及问题答案是这个用户的，并且是正确的
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.set("token_" + username, forgetToken);
            return ServerResponse.success(forgetToken);
        }
        return ServerResponse.error("问题的答案错误！");
    }


}
