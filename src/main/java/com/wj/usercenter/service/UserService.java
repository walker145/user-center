package com.wj.usercenter.service;

import com.wj.usercenter.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 王军
 * @description 用户服务
 * @createDate 2023-03-04 23:15:14
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param password      用户密码
     * @param checkPassword 校验密码
     * @return 用户id
     */
    long userRegister(String userAccount, String password, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param password     用户密码
     * @return 返回用户的脱敏信息
     */
    User doLogin(String userAccount, String password, HttpServletRequest request);

    /**
     * 用户信息脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);
}
