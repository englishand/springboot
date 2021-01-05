package com.zhy.test.utils;

import com.zhy.test.test.AAA;
import com.zhy.test.test.BBB;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Slf4j
public class InvokeMethodUtil {

    private static final String SET_METHOD_PREFIX="set";
    private static final String GET_METHOD_PREFIX="get";

    /**
     * 反射设置字段值
     * @param target
     * @param fieldName
     * @param param
     */
    public static void invokeSetMethod(Object target,String fieldName,Object param){
        if (param==null){
            throw new RuntimeException("参数值不能为空");
        }
        Class<?> targetClass = target.getClass();
        String methodName = SET_METHOD_PREFIX+ StringUtil.upperFirstChar(fieldName);
        try {
            Method setMethod = targetClass.getDeclaredMethod(methodName,param.getClass());
            setMethod.invoke(target,param);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            log.error("未在类型：{}中找到名为{}的方法",target,methodName);
        } catch (IllegalAccessException |InvocationTargetException e ) {
            e.printStackTrace();
            log.error(String.format("{}.{}方法执行错误",target,methodName),e);
        }
    }

    /**
     * 反射获取字段值
     * @param target
     * @param fieldName
     * @return
     */
    public static Object invokeGetMethod(Object target,String fieldName){
        if (StringUtils.isEmpty(fieldName)){
            throw new RuntimeException("对象属性不能为空");
        }
        Class<?> targetClass = target.getClass();
        String methodName = GET_METHOD_PREFIX+StringUtil.upperFirstChar(fieldName);
        try {
            Method getMethod = targetClass.getDeclaredMethod(methodName);
            return getMethod.invoke(target);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            log.error("未在类型：{}中找到名为{}的方法",target,methodName);
        } catch (IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
            log.error(String.format("{}.{}方法执行错误",target,methodName),e);
        }
        return null;
    }

    /**
     * 反射，属性值copy，null 不拷贝
     * @param target
     * @param source
     */
    public static void invokeCopy(Object target,Object source){
        Field[] targetFields = target.getClass().getDeclaredFields();
        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (Field sourceField:sourceFields){
            Object value = invokeGetMethod(source,sourceField.getName());
            if (value==null){
                continue;
            }
            for (Field targetField:targetFields){
                if (sourceField.getName().equals(targetField.getName())){
                    invokeSetMethod(target,targetField.getName(),value);
                }
            }
        }
    }

    public static void main(String[] args){
        AAA aaa = new AAA();
        BBB bbb = new BBB();
        InvokeMethodUtil.invokeCopy(aaa,bbb);
        System.out.println(aaa.toString());
    }

    /**
     * getFields();获取某个类的公共（pubLic）的字段，包括父类的字段。
     * getDeclaredFields();获取某个类的所有声明的字段，即包括public、private、protected,但不包括父类声明的字段。
     *
     * getMethod();获取当前类和父类的所有pubLic的方法。这里的父类，指的时继承层次中的所有父类。A继承B,B继承C,则这里B和C都为A的父类。
     * getDeclaredMethod();获取当前类的所有声明的方法。
     *
     * getConstructors();只返回public修饰的构造器。
     * getDeclaredConstructors();返回所有类型的构造器。
     */

}
