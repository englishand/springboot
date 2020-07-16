package com.zhy.test.service;

import com.zhy.test.utils.ResponseResult;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    ResponseResult loginIn(String username, String password, HttpServletRequest request);
}
