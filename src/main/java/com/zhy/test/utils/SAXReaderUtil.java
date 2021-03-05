package com.zhy.test.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAXReaderUtil {

    private Map<String,Object> map = new HashMap<String,Object>();

    /**
     * 解析模板文件
     */
    public void readXml(){
        //SAX解析类型
        SAXReader reader = new SAXReader();
        //读取模板
        File file = new File(System.getProperty("user.dir")+File.separator+"config"+File.separator+"msgTemp.xml");
        InputStream in = null;
        try {
            //读取配置文件
            in = new FileInputStream(file);
            //生成dom
            Document doc = reader.read(in);
            //获取节点
            Element root = doc.getRootElement();
            //解析节点
            resolveElement(root,"");
        }catch (Exception e){

        }
    }

    /**
     * 解析节点
     * @param root
     * @param prefix
     */
    public void resolveElement(Element root,String prefix){
        if (root == null){
            return;
        }
        List<Element> elements = root.elements();
        prefix += "\t";
        for (Element e:elements){
            map.put(e.attributeValue("id"),e.getTextTrim());
            resolveElement(e,prefix);
        }
    }

    public static void main(String[] args){
        SAXReaderUtil util = new SAXReaderUtil();
        util.readXml();
    }
}
