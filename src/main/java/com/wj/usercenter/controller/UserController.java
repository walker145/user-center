package com.wj.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wj.usercenter.model.domain.User;
import com.wj.usercenter.model.domain.request.UserLoginRequest;
import com.wj.usercenter.model.domain.request.UserRegisterLoginRequest;
import com.wj.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.wj.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.wj.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @Author: 王军
 * @version: 1.0
 * @Date: 2023-03-06  21:48
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 注册
     *
     * @param userRegisterLoginRequest
     * @return
     */
    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterLoginRequest userRegisterLoginRequest) {
        //校验
        if (userRegisterLoginRequest == null) {
            return null;
        }
        String userAccount = userRegisterLoginRequest.getUserAccount();
        String password = userRegisterLoginRequest.getPassword();
        String checkPassword = userRegisterLoginRequest.getCheckPassword();
        //校验
        if (StringUtils.isAnyBlank(userAccount, password, checkPassword)) {
            return null;
        }
        return userService.userRegister(userAccount, password, checkPassword);
    }

    /**
     * 登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        //校验
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String password = userLoginRequest.getPassword();
        //校验
        if (StringUtils.isAnyBlank(userAccount, password)) {
            return null;
        }
        return userService.doLogin(userAccount, password, request);
    }

    /**
     * 查询用户
     *
     * @param username
     * @return
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest servletRequest) {
        if (!isAdmin(servletRequest)){
            return new ArrayList<>();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNoneBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        /**
         * return userList.stream().map(user -> {
         *             user.setUserPassword(null);
         *             return userService.getSafetyUser(user);
         *         }).collect(Collectors.toList());
         */
        //优化后 ，先做设计，复用代码的优化，提取公共逻辑
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public boolean deleteUsers(@RequestBody long id, HttpServletRequest servletRequest) {
        if (!isAdmin(servletRequest)){
            return false;
        }
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }


    /**
     * 是否为管理员
     *
     * @param servletRequest
     * @return
     */
    public boolean isAdmin(HttpServletRequest servletRequest){
        //鉴权，仅观管理员可以删除
        Object userInfo = servletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userInfo;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }
}
