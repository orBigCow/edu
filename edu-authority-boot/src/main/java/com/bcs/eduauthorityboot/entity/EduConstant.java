package com.bcs.eduauthorityboot.entity;

/**
 常量内容说明
 */
public class EduConstant {

    // 状态码
    public static Integer ERROR_NOT_FOUND_PHONE_CODE = 1;
    public static Integer ERROR_PASSWORD_CODE = 2;
    public static Integer LOGIN_SUCCESS_CODE = 3;
    public static Integer TOKEN_SUCCESS_CODE = 4;
    public static Integer TOKEN_TIMEOUT_CODE = 5;
    public static Integer TOKEN_NULL_CODE = 6;
    public static Integer TOKEN_ERROR_CODE = 7;


    // 状态描述
    public static String ERROR_NOT_FOUND_PHONE = "该手机尚未注册";
    public static String ERROR_PASSWORD = "登录失败，帐号密码不匹配";
    public static String LOGIN_SUCCESS = "登录成功";
    public static String TOKEN_SUCCESS = "令牌校验通过";
    public static String TOKEN_TIMEOUT = "令牌过期";
    public static String TOKEN_NULL = "令牌格式错误！或为空令牌";
    public static String TOKEN_ERROR = "校验失败,token令牌就是错误的";

}