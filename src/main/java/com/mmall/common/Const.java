package com.mmall.common;

/**
 * 公共词
 */
public class Const {

    /**
     * 当前用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**校验使用*/
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";

    /**
     * jiao
     */
    public interface Role {
        //普通用户
        int ROLE_CUSTOMER = 0;
        //管理员用户
        int ROLE_ADMIN = 1;
    }
}
