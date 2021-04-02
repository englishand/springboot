package com.zhy.exception.securityException;

import javax.security.sasl.AuthenticationException;

public class MyUsernameNotFoundException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public MyUsernameNotFoundException(String msg){
        super(msg);
    }

    public MyUsernameNotFoundException(String msg,Throwable t){
        super(msg,t);
    }
}
