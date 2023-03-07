package com.wj.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: 王军
 * @version: 1.0
 * @Date: 2023-03-06  22:00
 * <p>
 * 用户注册请求体
 */
@Data
public class UserRegisterLoginRequest implements Serializable {

    private static final long serialVersionUID = 6596869038571274927L;

    private String userAccount;

    private String password;

    private String checkPassword;
}
