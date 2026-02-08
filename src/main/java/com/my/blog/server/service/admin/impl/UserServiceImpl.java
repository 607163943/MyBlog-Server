package com.my.blog.server.service.admin.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.my.blog.common.enums.ExceptionEnums;
import com.my.blog.common.exception.admin.AdminUserException;
import com.my.blog.common.utils.JWTUtils;
import com.my.blog.pojo.dto.admin.UserInfoDTO;
import com.my.blog.pojo.dto.admin.UserLoginDTO;
import com.my.blog.pojo.dto.admin.UserPasswordDTO;
import com.my.blog.pojo.po.User;
import com.my.blog.pojo.vo.admin.CaptchaVO;
import com.my.blog.pojo.vo.UserInfoVO;
import com.my.blog.pojo.vo.admin.UserLoginVO;
import com.my.blog.server.mapper.UserMapper;
import com.my.blog.server.service.admin.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private JWTUtils jwtUtils;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return 用户信息+令牌
     */
    @Override
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 验证码校验
        validateCaptchaCode(userLoginDTO.getCaptchaCode(), userLoginDTO.getCaptchaKey());

        // 用户校验
        User user = lambdaQuery().eq(User::getUsername, userLoginDTO.getUsername()).one();
        if (user == null) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_NOT_EXIST);
        }

        // 密码校验
        String passwordMd5 = SecureUtil.md5(userLoginDTO.getPassword());
        if (!passwordMd5.equals(user.getPassword())) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_PASSWORD_ERROR);
        }

        // 登陆成功
        log.info("用户{},id:{}，登陆成功！", user.getUsername(), user.getId());
        // 用户信息
        UserInfoVO userInfoVO = BeanUtil.copyProperties(user, UserInfoVO.class);

        // 生成令牌
        String token = jwtUtils.createJWTWithUserId(user.getId());

        // 更新用户登陆时间
        user.setLastLoginTime(LocalDateTime.now());
        updateById(user);

        // 令牌存入Redis 1天过期
        redisTemplate.opsForValue().set("user:token:" + user.getId(), token, 1, TimeUnit.DAYS);

        return UserLoginVO.builder()
                .token(token)
                .userInfoVO(userInfoVO)
                .build();
    }

    /**
     * 验证码校验
     * @param captchaCode 验证码
     * @param captchaKey 验证码key
     */
    private void validateCaptchaCode(String captchaCode,String captchaKey) {
        // 验证码校验
        Object captchaCodeObj = redisTemplate.opsForValue().get("user:captcha:" + captchaKey);
        if (captchaCodeObj == null) {
            // 验证码过期
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_CAPTCHA_EXPIRED);
        }

        String redisCaptchaCode = captchaCodeObj.toString();
        // 忽略大小写
        if (!captchaCode.equalsIgnoreCase(redisCaptchaCode)) {
            // 验证码错误
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_CAPTCHA_CODE_ERROR);
        }
        // 清除验证码缓存
        redisTemplate.delete("user:captcha:" + captchaKey);
    }

    /**
     * 获取验证码
     *
     * @return 验证码数据
     */
    @Override
    public CaptchaVO captcha() {
        ShearCaptcha shearCaptcha = CaptchaUtil.createShearCaptcha(150, 30, 4, 2);

        String imageBase64 = shearCaptcha.getImageBase64();

        // 验证码存入Redis
        // 用UUID作为唯一key
        String uuid = IdUtil.simpleUUID();
        redisTemplate.opsForValue().set("user:captcha:" + uuid, shearCaptcha.getCode(), 60, TimeUnit.SECONDS);

        return CaptchaVO.builder()
                .imageBase64(imageBase64)
                .captchaKey(uuid)
                .build();
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        // 获取登录用户数据
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        // 清除缓存中的token
        redisTemplate.opsForValue().getAndDelete("user:token:" + user.getId());

        log.info("用户{},id:{}，登出成功！", user.getUsername(), user.getId());
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @Override
    public UserInfoVO userInfo() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return BeanUtil.copyProperties(user, UserInfoVO.class);
    }

    /**
     * 修改用户信息
     * @param userInfoDTO 用户信息
     * @return 修改后的用户信息
     */
    @Override
    public UserInfoVO updateUserInfo(UserInfoDTO userInfoDTO) {
        // 判断登录用户和修改用户是否为同一人
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        if(!userInfoDTO.getId().equals(user.getId())) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_NOT_MATCH);
        }

        lambdaUpdate()
                .set(User::getUsername, userInfoDTO.getUsername())
                .set(User::getNickname, userInfoDTO.getNickname())
                .eq(User::getId, userInfoDTO.getId())
                .update();

        // 获取修改后用户信息
        user = getById(userInfoDTO.getId());
        return BeanUtil.copyProperties(user, UserInfoVO.class);
    }

    /**
     * 修改用户密码
     * @param userPasswordDTO 用户密码
     */
    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        // 验证码校验
        validateCaptchaCode(userPasswordDTO.getCaptchaCode(), userPasswordDTO.getCaptchaKey());

        User user = (User) SecurityUtils.getSubject().getPrincipal();

        // 用户旧密码校验
        String oldPasswordMd5 = SecureUtil.md5(userPasswordDTO.getOldPassword());
        if(!user.getPassword().equals(oldPasswordMd5)) {
            throw new AdminUserException(ExceptionEnums.ADMIN_USER_PASSWORD_ERROR);
        }

        // 修改用户密码
        String newPasswordMd5 = SecureUtil.md5(userPasswordDTO.getNewPassword());
        lambdaUpdate()
                .set(User::getPassword,newPasswordMd5)
                .eq(User::getId,user.getId())
                .update();

        log.info("用户{},id:{}，修改密码成功！", user.getUsername(), user.getId());

        // 清除redis token缓存
        redisTemplate.delete("user:token:" + user.getId());
    }
}
