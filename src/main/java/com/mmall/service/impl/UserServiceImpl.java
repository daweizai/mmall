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
            TokenCache.set(TokenCache.TOKEN_PREFIX + username, forgetToken);
            return ServerResponse.success(forgetToken);
        }
        return ServerResponse.error("问题的答案错误！");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.error("参数错误，token不能为空！");
        }
        //检验用户名
        ServerResponse<String> checkUsername = this.checkValid(username, Const.USERNAME);
        if (checkUsername.isSuccess()) {
            return ServerResponse.error("用户不存在");
        }
        String token = TokenCache.get(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.isBlank(token)) {
            return ServerResponse.error("Token无效或已过期");
        }
        if (StringUtils.isBlank(passwordNew)) {
            return ServerResponse.error("新密码不能为空");
        }

        if (StringUtils.equals(forgetToken, token)) {
            String password = MD5Util.MD5EncodeUtf8(passwordNew);
            int rowCount = userDao.updatePasswordByUsername(username, password);
            if (rowCount > 0) {
                return ServerResponse.successByMsg("修改密码成功");
            }
        } else {
            return ServerResponse.error("token错误，请重新获取重置密码的token");
        }
        return ServerResponse.error("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {
        //防止横向越权，要校验一下这个用户的旧密码，一定是指定用户的，因为我们会查询一个count(1),如果不指定id，那么结果就是true。
        int recount = userDao.checkPassword(passwordOld, user.getId());
        if (recount == 0) {
            return ServerResponse.error("旧密码错误！");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userDao.updateByPrimaryKeySelective(user);
        if (updateCount > 0) {
            return ServerResponse.successByMsg("密码更新成功！");
        }
        return ServerResponse.error("密码更新失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        int resultCount = userDao.checkEmailByUserId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.error("该邮箱已存在，请更换未使用过的Email");
        }

        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userDao.updateByPrimaryKeySelective(updateUser);
        if (updateCount > 0) {
            return ServerResponse.success(updateUser, "更新个人信息成功");
        }
        return ServerResponse.error("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer userId) {
        User user = userDao.selectByPrimaryKey(userId);
        if (user == null) {
            return ServerResponse.error("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success(user);
    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole().intValue() == Const.Role.ROLE_ADMIN) {
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }


}
