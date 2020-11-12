package com.zhy.test.utils;

import org.thymeleaf.util.StringUtils;

public class SystemPropertiesUtil {

    private static final String JAVA_VERSION="java.version";//Java运行时环境变量
    private static final String JAVA_VENDOR="java.vendor";//Java运行时环境供应商
    private static final String JAVA_VENDOR_URL="java.vendor.url";//Java供应商的url
    private static final String JAVA_HOME="java.home";//Java 安装目录

    private static final String JAVA_VM_SPECIFICATION_VERSION="java.vm.specification.version";//Java虚拟机规范名称
    private static final String JAVA_VM_VERSION="java.vm.version";//Java虚拟机实现版本
    private static final String JAVA_VM_VENDOR="java.vm.vendor";//Java虚拟机实现供应商
    private static final String JAVA_VM_NAME="java.vm.name";//Java 虚拟机名称

    public final static String JAVA_SPECIFICATION_VERSION = "java.specification.version"; // Java运行时环境规范版本
    public final static String JAVA_SPECIFICATION_VENDOR = "java.specification.vendor"; // Java运行时环境规范供应商
    public final static String JAVA_SPECIFICATION_NAME = "java.specification.name"; // Java运行时环境规范名称

    public final static String JAVA_CLASS_VERSION = "java.class.version"; // Java类格式版本号
    public final static String JAVA_CLASS_PATH = "java.class.path"; // Java 类路径
    public final static String JAVA_LIBRARY_PATH = "java.library.path"; // 加载库时搜索的路径列表7款发型秀出完美脸型
    public final static String JAVA_IO_TMPDIR = "java.io.tmpdir"; // 默认的临时文件路径
    public final static String JAVA_COMPILER = "java.compiler"; // 要使用的 JIT编译器的名称
    public final static String JAVA_EXT_DIRS = "java.ext.dirs"; // 一个或多个扩展目录的路径

    public final static String OS_NAME = "os.name"; // 操作系统的名称
    public final static String OS_ARCH = "os.arch"; // 操作系统的架构
    public final static String OS_VERSION = "os.version"; // 操作系统的版本

    public final static String FILE_SEPARATOR = "file.separator"; // 文件分隔符（在UNIX系统中是“/”）
    public final static String PATH_SEPARATOR = "path.separator"; // 路径分隔符（在UNIX系统中是“:”）
    public final static String LINE_SEPARATOR = "line.separator"; // 行分隔符（在 UNIX系统中是“/n”）

    public final static String USER_NAME = "user.name"; // 用户的账户名称
    public final static String USER_HOME = "user.home"; // 用户的主目录
    public final static String USER_DIR = "user.dir"; // 用户的当前工作目录

    public static String getPropertyInfo(String property){
        if (!StringUtils.isEmpty(property)){
            return System.getProperty(property);
        }
        return null;
    }

    public static void main(String[] args){
        String property = SystemPropertiesUtil.FILE_SEPARATOR;
        System.out.println(SystemPropertiesUtil.getPropertyInfo(property));
    }
}
