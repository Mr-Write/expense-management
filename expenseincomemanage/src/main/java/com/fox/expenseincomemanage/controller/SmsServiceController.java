package com.fox.expenseincomemanage.controller;


import com.alibaba.fastjson.JSONObject;
import com.fox.expenseincomemanage.entity.Result;
import com.fox.expenseincomemanage.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 短信服务
 *
 * @author 狐狸半面添
 * @create 2023-01-17 23:37
 */
@RestController
@RequestMapping("/sms")
public class SmsServiceController {
    @Resource
    private UserService userService;

    /**
     * 发送用于注册的验证码
     *
     * @param object 手机号的json对象
     * @return 发送情况
     */
    @PostMapping("/user/sendRegisterCode")
    public Result sendUserRegisterCode(@RequestBody JSONObject object) {
        String phone = object.getString("phone");
        return userService.sendUserRegisterCode(phone);
    }

}
