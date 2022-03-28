package com.bcs.eduauthorityboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bcs.eduauthorityboot.entity.EduConstant;
import com.bcs.eduauthorityboot.entity.User;
import com.bcs.eduauthorityboot.entity.UserDTO;
import com.bcs.eduauthorityboot.mapper.UserMapper;
import com.bcs.eduauthorityboot.service.UserService;
import com.bcs.eduauthorityboot.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     *  用户登录
     * @param phone  手机号
     * @param password  密码
     * @return
     */
    public UserDTO login(String phone, String password) {
        UserDTO dto = new UserDTO();
        // 创建条件构造
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        // 先检查手机号是否存在
        queryWrapper.eq("phone", phone);
        Integer phoneCount = userMapper.selectCount(queryWrapper);
        if (phoneCount == 0) {
            // 手机号不存在， 说明没有注册过
            dto.setState(EduConstant.ERROR_NOT_FOUND_PHONE_CODE);
            dto.setMessage(EduConstant.ERROR_NOT_FOUND_PHONE);
        } else {
            // 手机号存在,已经注册过 判断密码是否正确
            queryWrapper.eq("password", password);
            User user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                // 密码错误
                dto.setState(EduConstant.ERROR_PASSWORD_CODE);
                dto.setMessage(EduConstant.ERROR_PASSWORD);
            } else {
                // 密码正确
                dto.setState(EduConstant.LOGIN_SUCCESS_CODE);
                dto.setMessage(EduConstant.LOGIN_SUCCESS);
                dto.setContent(user);
                // 生成token
                String token = JwtUtil.createToken(user);
                dto.setToken(token);
                // 将token和用户信息放入redis中  并且设置生命周期为10分钟
                redisTemplate.opsForValue().set(token, token, 600, TimeUnit.SECONDS);
            }
        }
        return dto;
    }

    /**
     *   检查token是否正确
     * @param token  待校验的token
     * @return
     */
    @Override
    public UserDTO<User> checkToken(String token) {
        UserDTO<User> userDTO = new UserDTO<>();
        int verify = JwtUtil.isVerify(token);
        if (verify == 0) {
            // 校验通过
            userDTO.setState(EduConstant.TOKEN_SUCCESS_CODE);
            userDTO.setMessage(EduConstant.TOKEN_SUCCESS);
            // 重新设置redis中token的生命周期   10分钟
            redisTemplate.opsForValue().set(token, token, 600, TimeUnit.SECONDS);
        } else if (verify == 1) {
            // 令牌过期
            userDTO.setState(EduConstant.TOKEN_TIMEOUT_CODE);
            userDTO.setMessage(EduConstant.TOKEN_TIMEOUT);
        } else if (verify == 2) {
            // 令牌格式错误！或为空令牌！
            userDTO.setState(EduConstant.TOKEN_NULL_CODE);
            userDTO.setMessage(EduConstant.TOKEN_NULL);
        } else if (verify == 3) {
            // 校验失败,token令牌就是错误的
            userDTO.setState(EduConstant.TOKEN_ERROR_CODE);
            userDTO.setMessage(EduConstant.TOKEN_ERROR);
        }
        return userDTO;
    }

    /**
     *  用户注册
     * @param phone  手机号
     * @param password  密码
     * @param name  昵称
     * @param portrait  头像
     * @return
     */
    @Override
    public int register(String phone, String password, String name, String portrait) {
        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setPassword(password);
        user.setPortrait(portrait);
        return userMapper.insert(user);
    }

    /**
     *   手机号登录
     * @param phoneNumber  手机号
     * @return
     */
    @Override
    public UserDTO loginPhoneSms(String phoneNumber) {
        UserDTO<User> userDTO = new UserDTO<>();
        // 根据手机号查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phoneNumber);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            // 手机号没有注册过，先注册再登陆    密码和手机号一致
            register(phoneNumber, phoneNumber, "手机用户" + phoneNumber, "xxx");
            return loginPhoneSms(phoneNumber);
        }
        // 手机号已经注册过，直接登录成功
        userDTO.setState(EduConstant.LOGIN_SUCCESS_CODE);
        userDTO.setMessage(EduConstant.LOGIN_SUCCESS);
        // 创建token
        String token = JwtUtil.createToken(user);
        userDTO.setToken(token);
        // 将token和用户信息放入redis中  并且设置生命周期为10分钟
        redisTemplate.opsForValue().set(token, token, 600, TimeUnit.SECONDS);
        return userDTO;
    }
}
