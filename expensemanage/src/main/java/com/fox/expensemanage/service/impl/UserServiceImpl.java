package com.fox.expensemanage.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fox.expensemanage.constant.BasicConstants;
import com.fox.expensemanage.constant.HttpStatus;
import com.fox.expensemanage.constant.RedisConstants;
import com.fox.expensemanage.dao.UserMapper;
import com.fox.expensemanage.dto.UserSimpleInfoDTO;
import com.fox.expensemanage.entity.RedisUser;
import com.fox.expensemanage.entity.Result;
import com.fox.expensemanage.po.User;
import com.fox.expensemanage.service.UserService;
import com.fox.expensemanage.util.*;
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
    @Resource
    private RedisLockUtils redisLockUtils;

    @Override
    public Result sendUserRegisterCode(String phone) {

        // 1.校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.error(HttpStatus.HTTP_BAD_REQUEST.getCode(), "手机号格式错误");
        }

        // 2.查询该手机号是否已经被注册
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (count != 0) {
            // 说明已经被注册
            return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "该手机号已被注册");
        }

        String key = RedisConstants.LOGIN_USER_CODE_KEY + phone;

        // 3.查看缓存中是否已经存在，得到剩余TTL
        Long expire = redisCacheUtils.getExpire(key);

        // 4.存在并且剩余时长大于4分钟则不可再次发送验证码
        if (expire > RedisConstants.LOGIN_USER_CODE_AGAIN_TTL) {
            return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), "发送失败，验证码仍在有效期内");
        }

        // 5.验证码不存在或者剩余时长小于四分钟，则可以继续发送验证码 --> 先生成六位随机数
        String code = RandomUtil.randomNumbers(6);

        // 关于恶意并发的问题，在短信云平台已经自动做了处理，这里就无需处理

        // 6.先存储到 redis，附带验证次数，初始化为0
        redisCacheUtils.setCacheObject(key, code + ",0", RedisConstants.LOGIN_USER_CODE_TTL);

        // 7.发送短信到手机
        boolean result = txSmsTemplateUtils.sendRegisterCode(phone, code);
        if (!result) {
            // 7.1 发送失败，则移除 redis 中的验证码缓存信息，并返回
            redisCacheUtils.deleteObject(key + phone);
            return Result.error(HttpStatus.HTTP_INTERNAL_ERROR.getCode(), HttpStatus.HTTP_INTERNAL_ERROR.getValue());
        }

        // 7.2 发送成功
        return Result.ok();
    }

    @Override
    public Result register(String phone, String code, String password) {
        String lockKey = RedisConstants.LOCK_LOGIN_USER_KEY + phone;
        String codeKey = RedisConstants.LOGIN_USER_CODE_KEY + phone;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }
            // 3.查询键值对信息：cacheInfo[0] 是 code ，cacheInfo[1] 是 count
            String value = redisCacheUtils.getCacheObject(codeKey);
            if (value == null) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码已失效，请重新发送");
            }
            String[] cacheInfo = WaveStrUtils.strSplitToArr(value, ",");
            // 4.校验验证码
            if (!cacheInfo[0].equals(code)) {
                // 4.1 验证码不正确，count++
                int count = Integer.parseInt(cacheInfo[1]);
                count++;

                if (count >= BasicConstants.LOGIN_MAX_VERIFY_CODE_COUNT) {
                    // 次数大于等于设置的最大次数，验证码失效，移除 redis中 的验证码缓存
                    redisCacheUtils.deleteObject(codeKey);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "多次校验失败，验证码已失效，请重新发送");
                } else {
                    // 如果不是，就修改缓存中的信息
                    Long expire = redisCacheUtils.getExpire(codeKey);
                    if (expire > 0) {
                        redisCacheUtils.setCacheObject(codeKey, cacheInfo[0] + "," + count, expire);
                    }
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "验证码错误");
                }
            }

            // 4.2 校验成功，继续往后走

            // 5.删除验证码缓存
            redisCacheUtils.deleteObject(codeKey);

            // 6.查看MySQL数据库是否有该用户信息
            Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
            if (count != 0) {
                return Result.error(HttpStatus.HTTP_REFUSE_OPERATE.getCode(), "该手机号已被注册过");
            }

            // 7.创建新用户
            User user = new User();
            // 手机号
            user.setPhone(phone);
            // 昵称
            user.setNickName("逐浪者" + RandomUtil.randomString(8));
            // 默认头像
            user.setIcon(BasicConstants.DEFAULT_USER_ICON);
            // 加密密码
            user.setPassword(PasswordEncoderUtils.encode(password));

            userMapper.insert(user);

            // 8.返回
            return Result.ok();

        } finally {
            // 10.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }

    }

    @Override
    public Result login(String phone, String password) {
        String lockKey = RedisConstants.LOCK_LOGIN_USER_KEY + phone;
        String phoneKey = RedisConstants.LOGIN_USER_PWD_KEY + phone;
        boolean lock = false;
        try {
            // 1.拿到锁，设置TTL
            lock = redisLockUtils.tryLock(lockKey, RedisConstants.LOCK_LOGIN_USER_CODE_TTL);
            // 2.获取锁失败，直接退出
            if (!lock) {
                return Result.error(HttpStatus.HTTP_TRY_AGAIN_LATER.getCode(), HttpStatus.HTTP_TRY_AGAIN_LATER.getValue());
            }
            // 3.从 redis 中获取当前手机号的登录次数
            Integer count = redisCacheUtils.getCacheObject(phoneKey);
            if (count == null) {
                count = 0;
            }
            // 4.次数超过8次，则返回稍后再试
            if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_USER_PWD_LOCK_TTL / 60 + "分钟");
            }
            count++;
            // 5.从数据库中查询该手机号的用户信息
            User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
            // 6.用户信息不存在则返回错误
            if (user == null) {
                return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
            }

            // 7.校验密码
            Boolean matches = PasswordEncoderUtils.matches(user.getPassword(), password);
            if (!matches) {
                // 8.校验失败的处理
                // 8.1 是否冻结手机号
                if (count >= BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT) {
                    // 冻结手机+密码方式登录15分钟
                    redisCacheUtils.setCacheObject(phoneKey, count, RedisConstants.LOGIN_USER_PWD_LOCK_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "账号因多次输入密码错误，已冻结" + RedisConstants.LOGIN_USER_PWD_LOCK_TTL / 60 + "分钟");
                }
                // 8.2 未冻结，则修改将手机号验证次数
                if (count == 1) {
                    redisCacheUtils.setCacheObject(phoneKey, count, RedisConstants.LOGIN_USER_PWD_TTL);
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
                }
                Long expire = redisCacheUtils.getExpire(phoneKey);
                if (expire > 0) {
                    redisCacheUtils.setCacheObject(phoneKey, count, expire);
                }
                // 8.3 返回结果，当剩1-3次机会时返回剩余次数
                int surplusCount = BasicConstants.LOGIN_MAX_VERIFY_PWD_COUNT - count;
                if (surplusCount > 0 && surplusCount <= BasicConstants.LOGIN_PWD_MAX_SURPLUS_COUNT) {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误，" + surplusCount + "次机会后冻结手机号15分钟");
                } else {
                    return Result.error(HttpStatus.HTTP_UNAUTHORIZED.getCode(), "手机号或密码错误");
                }
            }
            // 校验成功则继续往后走

            // 9.移除缓存中的次数统计
            redisCacheUtils.deleteObject(phoneKey);

            // 10.缓存用户信息，携带 token 返回成功，以后请求时前端需要携带 token，放在请求头中
            return Result.ok(saveRedisInfo(user));
        } finally {
            // 13.最后释放锁
            if (lock) {
                redisLockUtils.unlock(lockKey);
            }
        }

    }

    @Override
    public Result getSelfSimpleInfo() {
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        return Result.ok(new UserSimpleInfoDTO(redisUser.getName(), redisUser.getIcon()));
    }

    @Override
    public Result modifyUserInfo(String nickName, String icon) {
        // 1.获取缓存信息
        RedisUser redisUser = UserHolderUtils.getRedisUser();
        User user = new User();
        // 2.判断是否有要修改的信息
        if (nickName != null && icon != null) {
            return Result.ok();
        }

        // 3.对需要修改的信息进行处理
        if (nickName != null) {
            user.setNickName(nickName);
            redisUser.setName(nickName);
        }
        if (icon != null) {
            user.setIcon(icon);
            redisUser.setIcon(icon);
        }

        // 4.修改数据库信息
        userMapper.updateById(user);
        // 5.修改缓存信息
        redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_USER_INFO_TTL);

        // 6.返回ok
        return Result.ok();
    }

    @Override
    public Result modifyPwd(String password, String code) {
        // 1.校验验证码是否正确
        String realCode = redisCacheUtils.getCacheObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
        // 1.1 不存在则返回
        if (realCode == null) {
            return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码已失效，请重新获取");
        }
        // 1.2 存在但不一致则清除并返回（忽略大小写）
        if (!realCode.equalsIgnoreCase(code)) {
            redisCacheUtils.deleteObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
            return Result.error(HttpStatus.HTTP_VERIFY_FAIL.getCode(), "验证码错误，已重新更新");
        }

        // 2.验证码正确，则从缓存中移除
        redisCacheUtils.deleteObject(RedisConstants.PWD_CODE_KEY + UserHolderUtils.getUserId());
        // 3.修改密码
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId, UserHolderUtils.getUserId())
                .set(User::getPassword, PasswordEncoderUtils.encode(password));
        this.update(wrapper);
        // 4.返回
        return Result.ok();
    }

    /**
     * 设置用户信息，缓存到 redis 中
     *
     * @param user 用户基本信息
     * @return 生成的token
     */
    public String saveRedisInfo(User user) {
        // todo 1.查询权限信息（后面再来补充）

        // 2.生成 token
        String uuid = UUID.randomUUID().toString(true);
        String token = CipherUtils.encrypt(user.getId() + "-" + uuid);

        // 3.设置缓存的用户信息
        RedisUser redisUser = new RedisUser();
        redisUser.setUuid(uuid);
        redisUser.setTime(System.currentTimeMillis());
        redisUser.setIcon(user.getIcon());
        redisUser.setName(user.getNickName());
        redisUser.setId(user.getId());
        // todo 设置权限

        // 4.信息存储到 redis
        redisCacheUtils.setCacheObject(RedisConstants.LOGIN_USER_INFO_KEY + redisUser.getId(), redisUser, RedisConstants.LOGIN_USER_INFO_TTL);

        // 5.返回token
        return token;
    }
}
