package com.zhy.test.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhy.test.entity.Student;
import com.zhy.test.service.Impl.TestServiceImpl;
import com.zhy.test.utils.httpRequest.AsyncPostUtil;
import com.zhy.test.utils.httpRequest.AsyncPostUtil2;
import com.zhy.test.utils.httpRequest.HttpPostUtil;
import com.zhy.test.utils.JsonAndXmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("annotation")
public class AnnotationController {

    private static final Logger logg = LoggerFactory.getLogger(AnnotationController.class);

    @Autowired
    protected Student student;
    @Autowired
    private TestServiceImpl testService;

    @ResponseBody
    @RequestMapping("getValueString/{username}")
    public JSONObject getValueString(@PathVariable("username")String username, HttpServletRequest request){
        JSONObject js = new JSONObject();
        js.put("value", student.getValueString());
        String msg = null;
        String password = null;
        String reqContent = null;
        try {
            //通过post.setEntity(StringEntity)传递，接收方式
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String s = null;
            while ((s=br.readLine())!=null){
                sb.append(s);
            }
            logg.info("通过text/xml接收到的报文为：{}",sb.toString());

            //通过post.setEntity(urlEncodedFormEntity),接受方式
            String code = request.getParameter("transCode");
            String pass = request.getParameter("password");
            pass = pass==null?"":pass;
            password = code+pass;
            msg = testService.getMsg();
        } catch (Exception e) {
            msg = "异常处理信息";
            logg.info(e.getMessage());
        }
        js.put("msg",msg);
        js.put(username,password);
        return js;
    }

    @ResponseBody
    @RequestMapping("testRequestBodyWithJson")
    public JSONObject testRequestBody(@RequestBody String json){
        JSONObject js = JSONObject.parseObject(json);
        return js;
    }

    @ResponseBody
    @RequestMapping("testRequestBodyWithXml")
    public JSONObject testRequestBodyWithXml(@RequestBody String xml){
        String jsonStr = JsonAndXmlUtil.xmlToJson(xml);
        JSONObject js = null;
        try {
            js = JSONObject.parseObject(jsonStr)
                    .getJSONObject("soapenv:Envelope").getJSONObject("soapenv:Body")
//                    .getJSONObject("S05800101BMS1001").getJSONObject("RequestBody");
                    .getJSONObject("cqr:MCR1002").getJSONObject("RequestBody");
        }catch (Exception e){
            logg.error("解析xml异常",e);
            js.put("error",e.getMessage());
        }finally {
            return js;
        }
    }

    @ResponseBody
    @RequestMapping(value = "testBindingResult")
    public JSONObject testBindingReult(@Valid @RequestBody Student student, BindingResult bindingResult){
        JSONObject js = new JSONObject();
        if (bindingResult.hasErrors()){
            List<FieldError> errorList = bindingResult.getFieldErrors();
            StringBuilder sb = new StringBuilder();
            for (int i=0;i<errorList.size();i++){
                sb.append(errorList.get(i).getDefaultMessage());
                sb.append(";");
            }
            js.put("errormessage",sb);
            logg.info(bindingResult.toString());
        }else {
            js.put("getStudent", student);
        }
        return js;
    }


    @ResponseBody
    @RequestMapping("testAsyncPostUtil")
    public JSONObject testAsyncPostUtil(){
        String result = new HttpPostUtil().post();
        String result2 = new AsyncPostUtil2().sendRequest();
        String result3 = "";
        try {
            result3 = new AsyncPostUtil().sendPostRequest(400,"UTF-8");
        } catch (ExecutionException e) {
            logg.info(e.getMessage());
            result3 = e.getMessage();
        } catch (InterruptedException e) {
            logg.info("线程中断");
            result3 = "线程中断";
        }

        JSONObject js = new JSONObject();
        js.put("result",result);
        js.put("result2",result2);
        js.put("result3",result3);
        return js;
    }


}
