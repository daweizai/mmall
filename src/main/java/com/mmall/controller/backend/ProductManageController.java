package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;

/**
 * 商品控制器
 *
 * @Author daweizai
 * @Date 21:57 2020/6/17
 * @ClassName ProductManageController
 * @Version 1.0
 **/
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;

    @PostMapping("save.do")
    @ResponseBody
    public ServerResponse<String> productSave(@ApiIgnore HttpSession session, Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //添加商品的业务逻辑
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }


    @PostMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse<String> setSaleStatus(@ApiIgnore HttpSession session, Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iProductService.setSaleStatus(productId, status);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }

    @PostMapping("detail.do")
    @ResponseBody
    public ServerResponse<String> getDetail(@ApiIgnore HttpSession session, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return null;
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }
}
