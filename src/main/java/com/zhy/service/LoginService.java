package com.zhy.service;

import com.zhy.utils.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    ResponseResult loginIn(String username, String password, HttpServletRequest request);
}
