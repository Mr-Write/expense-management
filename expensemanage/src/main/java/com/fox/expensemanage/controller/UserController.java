package com.fox.expensemanage.controller;


import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.service.UserService;
import com.fox.expensemanage.vo.UserInfoUpdateVO;
import com.fox.expensemanage.vo.UserLoginVO;
import com.fox.expensemanage.vo.UserRegisterVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-14
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    /**
     * 用户注册
     *
     * @param userRegisterVO 手机号 + 验证码 + 密码
     * @return 注册状况
     */
    @PostMapping("/register")
    public Result register(@Validated @RequestBody UserRegisterVO userRegisterVO) {
        return userService.register(userRegisterVO.getPhone(), userRegisterVO.getCode(), userRegisterVO.getPassword());
    }

    /**
     * 用户登录
     *
     * @param userLoginVO 手机号 + 密码
     * @return 登录状况
     */
    @PostMapping("/login")
    public Result login(@Validated @RequestBody UserLoginVO userLoginVO) {
        return userService.login(userLoginVO.getPhone(), userLoginVO.getPassword());
    }

    /**
     * 获取个人简单信息
     *
     * @return 信息
     */
    @GetMapping("/getSelfSimpleInfo")
    public Result getSelfSimpleInfo(){
        return userService.getSelfSimpleInfo();
    }

    /**
     * 修改用户信息
     *
     * @param userInfoUpdateVO 昵称 + 头像
     * @return 修改状况
     */
    @PutMapping("/modifyUserInfo")
    public Result modifyUserInfo(@Validated @RequestBody UserInfoUpdateVO userInfoUpdateVO){
        return userService.modifyUserInfo(userInfoUpdateVO.getNickName(),userInfoUpdateVO.getIcon());
    }
}
