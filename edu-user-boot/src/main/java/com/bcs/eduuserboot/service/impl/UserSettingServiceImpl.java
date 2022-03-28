package com.bcs.eduuserboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.bcs.eduuserboot.entity.User;
import com.bcs.eduuserboot.mapper.UserSettingMapper;
import com.bcs.eduuserboot.service.UserSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingServiceImpl implements UserSettingService {

    @Autowired
    private UserSettingMapper userSettingMapper;

    /**
     *   修改用户信息 （用户名，头像地址）
     * @param userId  要修改信息的用户id
     * @param userName  新的用户名
     * @param headImg  新的头像地址
     */
    @Override
    public void updateUserInfo(int userId, String userName, String headImg) {
        User user = new User();
        user.setId(userId);
        if (userName != null && userName != "") {
            user.setName(userName);
        }
        if (headImg != null && headImg != "") {
            user.setPortrait(headImg);
        }
        userSettingMapper.updateById(user);
    }

    /**
     *   修改密码
     * @param userId  要修改密码的用户id
     * @param password  新密码
     */
    @Override
    public void updatePassword(int userId, String password) {
        User user = new User();
        user.setId(userId);
        user.setPassword(password);
        userSettingMapper.updateById(user);
    }
}
