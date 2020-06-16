package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 用户控制器（前端）
 */

@Controller
@RequestMapping("/user/")
@Api(tags = "用户控制器")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @param session  session
     */
    @PostMapping("login.do")
    @ResponseBody
    @ApiOperation(value = "登录接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, paramType = "query"),
    })
    public ServerResponse<User> login(String username, String password, @ApiIgnore HttpSession session) {
        ServerResponse<User> login = iUserService.login(username, password);
        if (login.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, login.getData());
        }
        return login;
    }

    /**
     * 登出接口
     */
    @PostMapping("logout.do")
    @ResponseBody
    @ApiOperation(value = "登出接口")
    public ServerResponse<String> logout(@ApiIgnore HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.success();
    }


    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @PostMapping("register.do")
    @ResponseBody
    @ApiOperation(value = "用户注册")
    public ServerResponse<String> register(User user) {
        if (user == null) {
            ServerResponse.error("用户注册信息不能空");
        }
        return iUserService.addUser(user);
    }

    /**
     * 验证用户是否存在
     *
     * @param value 需要验证的值
     * @param type  要检验的类型  email、username
     */
    @PostMapping("checkValid.do")
    @ResponseBody
    @ApiOperation(value = "验证用户是否存在")
    public ServerResponse<String> checkValid(String value, String type) {
        return iUserService.checkValid(value, type);
    }


    /**
     * 获取用户信息
     *
     * @param session
     * @return
     */
    @PostMapping("get_user_info.do")
    @ResponseBody
    @ApiOperation(value = "获取用户信息")
    public ServerResponse<User> getUserInfo(@ApiIgnore HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (null != user) {
            return ServerResponse.success(user);
        }
        return ServerResponse.error("用户未登录，无法获取当前用户的个人信息");
    }

    /**
     * 忘记密码功能
     *
     * @return
     */
    @PostMapping("forget_get_question.do")
    @ResponseBody
    @ApiOperation(value = "忘记密码功能")
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.selectQuestion(username);
    }


    /**
     * 检验问题的答案
     *
     * @param username 用户名
     * @param question 问题
     * @param answer   答案
     */
    @PostMapping("forget_check_answer.do")
    @ResponseBody
    @ApiOperation(value = "检验问题的答案")
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码，进行修改密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param forgetToken 验证答案后获取的验证token
     * @return
     */
    @PostMapping("forget_reset_password.do")
    @ResponseBody
    @ApiOperation(value = "忘记密码，进行修改密码")
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        return iUserService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 重置密码
     *
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @PostMapping("reset_password.do")
    @ResponseBody
    @ApiOperation(value = "重置密码")
    public ServerResponse<String> resetPassword(@ApiIgnore HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error("用户未登录");
        }
        return iUserService.resetPassword(passwordOld, passwordNew, user);
    }


    /**
     * 更新个人信息
     *
     * @param session
     * @param user
     * @return
     */
    @PostMapping("update_information.do")
    @ResponseBody
    @ApiOperation(value = "更新个人信息")
    public ServerResponse<User> updateInformation(@ApiIgnore HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.error("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> update = iUserService.updateInformation(user);
        if (update.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, update.getData());
        }

        return update;
    }

    /**
     * 获取个人信息
     *
     * @param session
     * @return
     */
    @PostMapping("get_information.do")
    @ResponseBody
    @ApiOperation(value = "获取个人信息")
    public ServerResponse<User> getInformation(@ApiIgnore HttpSession session) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "未登录，需要强制登录status=10");
        }
        return iUserService.getInformation(currentUser.getId());
    }


}
