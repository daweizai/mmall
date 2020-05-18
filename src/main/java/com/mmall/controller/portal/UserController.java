package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.MmallUser;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 用户控制器（前端）
 */
@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @PostMapping("/login.do")
    @ResponseBody
    public ServerResponse login(String username, String password, HttpSession session) {
        ServerResponse<MmallUser> login = iUserService.login(username, password);
        if (login.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, login.getData());
        }
        return login;
    }
}
