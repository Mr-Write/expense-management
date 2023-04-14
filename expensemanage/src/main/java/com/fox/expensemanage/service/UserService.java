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

    /**
     * 用户注册
     *
     * @param phone    手机号
     * @param code     验证码
     * @param password 密码
     * @return 注销状况
     */
    Result register(String phone, String code, String password);

    /**
     * 用户登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 登录状况
     */
    Result login(String phone, String password);

    /**
     * 获取个人简单信息
     *
     * @return 信息
     */
    Result getSelfSimpleInfo();

    /**
     * 修改用户信息
     *
     * @param nickName 昵称
     * @param icon     头像
     * @return 修改状况
     */
    Result modifyUserInfo(String nickName, String icon);

    /**
     * 修改密码
     *
     * @param password 新密码
     * @param code     验证码
     * @return 修改状况
     */
    Result modifyPwd(String password, String code);
}
