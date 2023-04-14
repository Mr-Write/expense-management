package com.fox.expensemanage.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fox.expensemanage.constant.HttpStatus;
import com.fox.expensemanage.constant.RedisConstants;
import com.fox.expensemanage.dao.UserMapper;
import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.po.User;
import com.fox.expensemanage.service.UserService;
import com.fox.expensemanage.util.RedisCacheUtils;
import com.fox.expensemanage.util.RegexUtils;
import com.fox.expensemanage.util.TxSmsTemplateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 狐狸半面添
 * @since 2023-04-15
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RedisCacheUtils redisCacheUtils;
    @Resource
    private TxSmsTemplateUtils txSmsTemplateUtils;
    @Resource
    private UserMapper userMapper;

    @Override
    public Result sendUserRegisterCode(String phone) {

        // 1.校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "手机号格式错误");
        }

        // 2.查询该手机号是否已经被注册
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (count!=0){
            // 说明已经被注册
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(),"该手机号已被注册");
        }

        String key = RedisConstants.LOGIN_USER_CODE_KEY + phone;

        // 3.查看缓存中是否已经存在，得到剩余TTL
        Long expire = redisCacheUtils.getExpire(key);

        // 4.存在并且剩余时长大于4分钟则不可再次发送验证码
        if(expire > RedisConstants.LOGIN_USER_CODE_AGAIN_TTL){
            return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(),"发送失败，验证码仍在有效期内");
        }

        // 5.验证码不存在或者剩余时长小于四分钟，则可以继续发送验证码 --> 先生成六位随机数
        String code = RandomUtil.randomNumbers(6);

        // 关于恶意并发的问题，在短信云平台已经自动做了处理，这里就无需处理

        // 6.先存储到 redis，附带验证次数，初始化为0
        redisCacheUtils.setCacheObject(key,code+",0",RedisConstants.LOGIN_USER_CODE_TTL);

        // 7.发送短信到手机
        boolean result = txSmsTemplateUtils.sendRegisterCode(phone,code);
        //boolean result = txSmsTemplateUtils.sendLoginCode(phone, code);
        if(!result){
            // 7.1 发送失败，则移除 redis 中的验证码缓存信息，并返回
            redisCacheUtils.deleteObject(key + phone);
            return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(),HttpStatus.HTTP_INTERNAL_ERROR.getValue());
        }

        // 7.2 发送成功
        return Result.ok();
    }
}
