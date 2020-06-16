package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
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
 * 分类控制器
 *
 * @Author daweizai
 * @Date 21:03 2020/6/12
 * @ClassName CategoryManageControoler
 * @Version 1.0
 **/
@Controller
@RequestMapping("/manage/category")
@Api(tags = "商品分类控制器")
public class CategoryManageController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加分类
     *
     * @param session      session
     * @param categoryName 分类名称
     * @param parentId     分类父节点id
     */
    @PostMapping("add_category.do")
    @ResponseBody
    @ApiOperation(value = "添加商品分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "categoryName", value = "分类名称", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "parentId", value = "父类id", dataType = "int", paramType = "query")
    })
    public ServerResponse addCategory(@ApiIgnore HttpSession session, String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        //验证是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }

    /**
     * 更新分类
     *
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @PostMapping("update_category.do")
    @ResponseBody
    @ApiOperation(value = "更新商品分类")
    public ServerResponse updateCategory(@ApiIgnore HttpSession session, String categoryName, Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        //验证是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            return iCategoryService.updateCategory(categoryName, categoryId);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }

    /**
     * 查询子节点的分类
     *
     * @param session
     * @param categoryId
     * @return
     */
    @GetMapping("get_children_category.do")
    @ResponseBody
    @ApiOperation(value = "查询子节点的分类")
    public ServerResponse getChildrenParallelCategory(@ApiIgnore HttpSession session,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        //验证是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询子节点的category信息，并且不递归，保持平级
            return iCategoryService.getChildrenParallelCategory(categoryId);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }

    /***
     * 获取子节点，包括递归子节点的id
     * @param session
     * @param categoryId
     * @return
     */
    @GetMapping("get_deep_category.do")
    @ResponseBody
    @ApiOperation(value = "获取子节点，包括递归子节点的id")
    public ServerResponse getCategoryAndDeepChildrenCategory(@ApiIgnore HttpSession session,
                                                             @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.error(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，请登录后在操作！");
        }
        //验证是否是管理员
        if (iUserService.checkAdminRole(user).isSuccess()) {
            //查询当前节点的id和递归子节点的id
            return iCategoryService.selectCategoryAndChildrenById(categoryId);
        } else {
            return ServerResponse.error("无权操作，需要管理员权限");
        }
    }


}
