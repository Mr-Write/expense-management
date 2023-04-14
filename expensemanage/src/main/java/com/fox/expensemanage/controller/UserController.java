package com.fox.expensemanage.controller;


import com.fox.expensemanage.service.UserService;
import com.fox.expensemanage.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
}
