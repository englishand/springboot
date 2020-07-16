package com.zhy.test.soapTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.io.*;

/**
 * 实现ServletContextAware可以获取到servletcontext
 */

@Component
public class SoapTemplateInit implements ServletContextAware {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${soap.dir}")
    private String soapDir;
    @Value("${soap.subfix}")
    private String subfix;

    @Value("${soap.blackList}")
    private String blackListFile;

    @Value("${soap.returnSoap}")
    private String returnSoapFile;

    @Value("${soap.MSG}")
    private String MSGFile;

    @Value("${soap.inJar}")
    private boolean inJar;

    public static String blackList;
    public static String returnsoap;
    public static String MSG;

    @Override
    public void setServletContext(ServletContext servletContext) {
        //以下两种将文件读到内存中的方式都可以。最后赋给静态变量。
        InputStream inputStream = servletContext.getClassLoader().getResourceAsStream(soapDir+ File.separator+blackListFile+subfix);
        //InputStream inputStream2 = Thread.currentThread().getContextClassLoader().getResourceAsStream(soapDir+File.separator+blackList+subfix);
        blackList = inputStreamToString(inputStream);

        InputStream inputStreamSoap = servletContext.getClassLoader().getResourceAsStream(soapDir+File.separator+returnSoapFile+subfix);
        returnsoap = inputStreamToString(inputStreamSoap);

        InputStream inputStreamMsg = servletContext.getClassLoader().getResourceAsStream(soapDir+File.separator+MSGFile+subfix);
        MSG = inputStreamToString(inputStreamMsg);
        logger.info("初始化黑名单接口模板为：{},返回soap模板为：{},MSG模板为：{}",blackList,returnsoap,MSG);
    }

    public String inputStreamToString(InputStream inputStream){
        StringBuilder sb = new StringBuilder(50);
        BufferedReader br =new BufferedReader(new InputStreamReader(inputStream));
        String len;
        try{
            while ((len = br.readLine())!=null){
                sb.append(len);
            }
            br.close();
        }catch (IOException e){
            logger.error("读取文件失败");
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                logger.error("关闭文件流异常",e);
            }
        }
        return sb.toString();
    }

}
