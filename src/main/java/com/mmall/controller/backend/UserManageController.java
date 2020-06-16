package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 用户管理控制器
 *
 * @Author daweizai
 * @Date 19:30 2020/6/10
 * @ClassName UserManageController
 * @Version 1.0
 **/
@Controller
@RequestMapping("/manage/user")
@Api(tags = {"用户管理控制器"})
public class UserManageController {

    @Autowired
    private IUserService iUserService;

    /**
     * 管理员登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("login.do")
    @ResponseBody
    @ApiOperation("管理员登录")
    public ServerResponse<User> login(String username, String password,  @ApiIgnore HttpSession session) {
        ServerResponse<User> login = iUserService.login(username, password);
        if (login.isSuccess()) {
            User user = login.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                //说明该用户是管理员
                session.setAttribute(Const.CURRENT_USER, user);
            } else {
                return ServerResponse.error("不是管理员，无法登录！");
            }
        }
        return login;
    }

}
