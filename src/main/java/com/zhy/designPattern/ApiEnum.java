package com.zhy.designPattern;

/**
 * 枚举定义接口信息
 */
public enum ApiEnum {
    API_01("api01.xml","接口01"),
    API_02("api02.xml","接口02");
    private String value;
    private String desc;
    public String getValue(){
        return value;
    }
    public String getDesc(){
        return desc;
    }
    ApiEnum(String value,String desc){
        this.value=value;
        this.desc=desc;
    }
}
