package com.zhy.test.controller;

import com.zhy.test.utils.ResponseResult;
import com.zhy.test.verifycode.entity.VerifyCode;
import com.zhy.test.verifycode.service.IVerifyCodeGen;
import com.zhy.test.verifycode.service.impl.SimpleCharVerifyCodeGenImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码调用controller
 */
@Slf4j
@Controller
@RequestMapping("verify")
public class VerifyCodeController {

    @RequestMapping("/toVerify")
    public String toVerify(){
        return "/verifycode/verifycode";
    }

    @RequestMapping("/verifyCode")
    public void verifyCode(HttpServletRequest request, HttpServletResponse response) {
        IVerifyCodeGen iVerifyCodeGen = new SimpleCharVerifyCodeGenImpl();
        try {
            //设置长宽
            VerifyCode verifyCode = iVerifyCodeGen.generate(80, 28);
            String code = verifyCode.getCode();
            log.info(code);
            //将VerifyCode绑定session
            request.getSession().setAttribute("VerifyCode", code);
            //设置响应头
            response.setHeader("Pragma", "no-cache");
            //设置响应头
            response.setHeader("Cache-Control", "no-cache");
            //在代理服务器端防止缓冲
            response.setDateHeader("Expires", 0);
            //设置响应内容类型
            response.setContentType("image/jpeg");
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            log.info("", e);
        }
//        ResponseResult result = ResponseResult.successWithData(code);
//        return result;
    }

    @RequestMapping("checkVerify")
    @ResponseBody
    public ResponseResult checkVerify(String inputCode,HttpServletRequest request){
        ResponseResult result;
        String code = (String) request.getSession().getAttribute("VerifyCode");
        if(inputCode.equals(code)){
            result = ResponseResult.success();
        }else {
            result = ResponseResult.error();
        }
        return result;
    }
}
