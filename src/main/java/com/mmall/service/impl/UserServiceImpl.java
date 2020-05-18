package com.mmall.service.impl;

import com.mmall.common.ServerResponse;
import com.mmall.dao.MmallUserDao;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private MmallUserDao mmallUserDao;

    @Override
    public ServerResponse<MmallUser> login(String username, String password) {
        //查询username的用户是否存在
        int resultCount = mmallUserDao.checkUsername(username);
        if (resultCount == 0) {
            return ServerResponse.error("用户不存在");
        }
        //TODO md5密码加密
        MmallUser user = mmallUserDao.selectByUsernameAndPassword(username, password);
        if (user == null) {
            return ServerResponse.error("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.success(user);
    }
}
