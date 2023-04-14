package com.fox.expensemanage.service;

import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-14
 */
public interface UserService extends IService<User> {

    /**
     * 发送用于注册的验证码
     *
     * @param phone 手机号
     * @return 发送情况
     */
    Result sendUserRegisterCode(String phone);
}
