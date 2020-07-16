package com.zhy.test.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
public class ResponseResult implements Serializable {

    private static final long serialVersionUID = -6592548838615372080L;
    private int code;
    private String message;
    private Object data;

    public static ResponseResult success(){
        ResponseResult result = new ResponseResult();
        result.code = 0;
        result.message = "操作成功！";
        return result;
    }

    public static ResponseResult successWithData(Object data){
        ResponseResult result = new ResponseResult();
        result.code = 0;
        result.message = "操作成功！";
        result.data = data;
        return result;
    }

    public static ResponseResult successWithMData(String message,Object data){
        ResponseResult result = new ResponseResult();
        result.code = 0;
        result.message = message;
        result.data = data;
        return result;
    }

    public static ResponseResult error(){
        ResponseResult result = new ResponseResult();
        result.code = 1;
        result.message = "操作失败！";
        return result;
    }

    public static ResponseResult errorWithMessage(String message){
        ResponseResult result = new ResponseResult();
        result.message = message;
        result.code = 1;
        return result;
    }

    public static ResponseResult errorWithCode(int code){
        ResponseResult result = new ResponseResult();
        result.code = code;
        return result;
    }

    public static ResponseResult successWithMessage(String message){
        ResponseResult result = new ResponseResult();
        result.message = message;
        result.code = 0;
        return result;
    }

    public String toString(){
        return "code:"+code+";message:"+message+";data:"+data;
    }
}
