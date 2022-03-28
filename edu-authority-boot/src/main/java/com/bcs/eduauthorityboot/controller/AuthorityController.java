package com.bcs.eduauthorityboot.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.bcs.eduauthorityboot.entity.User;
import com.bcs.eduauthorityboot.entity.UserDTO;
import com.bcs.eduauthorityboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class AuthorityController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    // 短信签名名称
    private String signName = "大佬孙";
    // 短信模板Code
    private String templateCode = "SMS_177536068";

    private String accessKeyId = "LTAI4FwKDkeZ6StZvRxg5RDf";

    private String assessKeySecret = "09IMDRUia2uIC7HMXpSmM5CiXuUgvf";

    /**
     *  用户登录
     * @param phone  账号手机号
     * @param password  密码
     * @return
     */
    @RequestMapping("/login")
    public UserDTO<User> login(String phone, String password) {
        return userService.login(phone, password);
    }

    /**
     *   检查token是否正确
     * @param token  待校验的token
     * @return
     */
    @RequestMapping("/checkToken")
    public UserDTO<User> checkToken(String token) {
        return userService.checkToken(token);
    }

    /**
     *   登出
     * @param token 登陆时的token
     */
    @RequestMapping("/logOut")
    public void logout(String token){
        redisTemplate.delete(token);
    }

    /**
     *   给手机发送验证码
     * @param phoneNumber
     */
    @RequestMapping("/sendSms")
    public Object sendSms(String phoneNumber){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, assessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        String code = "";
        // 随机生成一个6位数的验证码
        for(int i = 0; i<6; i++){
            code = code + (int)(Math.random()*9);
        }
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println("test");
            System.out.println(response.getData());
            String jsonStr = response.getData();
            System.out.println(jsonStr);
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            if("业务停机".equals(jsonObject.get("Message"))){
                jsonObject.put("phoneNumber", phoneNumber);
                jsonObject.put("smsCode", 21);
                return jsonObject;
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *   手机号登录
     * @param phoneNumber  手机号
     * @return
     */
    @GetMapping("loginPhoneSms")
    public UserDTO loginPhoneSms(String phoneNumber) {
        return userService.loginPhoneSms(phoneNumber);
    }

}
