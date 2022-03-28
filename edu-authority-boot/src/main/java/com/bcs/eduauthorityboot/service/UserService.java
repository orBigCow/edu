package com.bcs.eduauthorityboot.service;

import com.bcs.eduauthorityboot.entity.User;
import com.bcs.eduauthorityboot.entity.UserDTO;

public interface UserService {

    /**
     *  用户登录
     * @param phone  手机号
     * @param password  密码
     * @return
     */
    public UserDTO login(String phone, String password);

    /**
     *   检查token是否正确
     * @param token  待校验的token
     * @return
     */
    UserDTO<User> checkToken(String token);

    /**
     *  用户注册
     * @param phone  手机号
     * @param password  密码
     * @param name  昵称
     * @param portrait  头像
     * @return
     */
    int register(String phone, String password, String name, String portrait);

    /**
     *   手机号登录
     * @param phoneNumber  手机号
     * @return
     */
    UserDTO loginPhoneSms(String phoneNumber);
}
