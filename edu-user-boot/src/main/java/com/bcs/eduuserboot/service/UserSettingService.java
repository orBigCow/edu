package com.bcs.eduuserboot.service;

public interface UserSettingService {

    /**
     *   修改用户信息 （用户名，头像地址）
     * @param userId  要修改信息的用户id
     * @param userName  新的用户名
     * @param headImg  新的头像地址
     */
    void updateUserInfo(int userId, String userName, String headImg);

    /**
     *   修改密码
     * @param userId  要修改密码的用户id
     * @param password  新密码
     */
    void updatePassword(int userId, String password);
}
