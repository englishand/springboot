package com.zhy.test.jsonObject;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhy.test.utils.JsonAndXmlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class PrinterWriter {

    public String test(int i) throws Exception {
        if (i==1){
            System.out.println("是1");
            throw new Exception("错误");
        }
        System.out.println("是否海之星");
        i++;
        System.out.println(i);
        return "测试";
    }

    public void printwriter(HttpServletRequest request,HttpServletResponse response){

        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            PrintWriter writer = response.getWriter();
            writer.write("异常处理");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        PrinterWriter pw = new PrinterWriter();
        try {
            pw.test(1);
        } catch (Exception e) {
            System.out.println("22222222222");
            e.printStackTrace();
        }
            String str = "<?xml version='1.0' encoding=\"GB18030\"?>\n" +
                    "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/'\n" +
                    "                \txmlns:cqr='www.cqrcb.com.cn'>\n" +
                    "\t<soapenv:Header/>\n" +
                    "\t<soapenv:Body>\n" +
                    "\t\t<cqr:MCR1002>\n" +
                    "\t\t\t<RequestHeader>\n" +
                    "\t\t\t\t<VerNo>20121016ESB</VerNo>\n" +
                    "\t\t\t\t<ReqSysCd>026001</ReqSysCd>\n" +
                    "\t\t\t\t<ReqSecCd>026001</ReqSecCd>\n" +
                    "\t\t\t\t<TxnTyp>RQ</TxnTyp>\n" +
                    "\t\t\t\t<TxnMod>0</TxnMod>\n" +
                    "\t\t\t\t<TxnCd>MCR1002</TxnCd>\n" +
                    "\t\t\t\t<TxnNme/>\n" +
                    "\t\t\t\t<ReqDt>2020-05-25</ReqDt>\n" +
                    "\t\t\t\t<ReqTm>2020-05-2515:20:48</ReqTm>\n" +
                    "\t\t\t\t<ReqSeqNo>W12020052542065175</ReqSeqNo>\n" +
                    "\t\t\t\t<ChnlNo>W1</ChnlNo>\n" +
                    "\t\t\t\t<BrchNo>7777</BrchNo>\n" +
                    "\t\t\t\t<BrchNme/>\n" +
                    "\t\t\t\t<TrmNo/>\n" +
                    "\t\t\t\t<TrmIP/>\n" +
                    "\t\t\t\t<TlrNo>5142</TlrNo>\n" +
                    "\t\t\t\t<TlrNme/>\n" +
                    "\t\t\t\t<TlrLvl/>\n" +
                    "\t\t\t\t<TlrTyp/>\n" +
                    "\t\t\t\t<TlrPwd/>\n" +
                    "\t\t\t\t<AuthTlr/>\n" +
                    "\t\t\t\t<AuthPwd/>\n" +
                    "\t\t\t\t<AuthCd/>\n" +
                    "\t\t\t\t<AuthFlg/>\n" +
                    "\t\t\t\t<AuthDisc/>\n" +
                    "\t\t\t\t<AuthWk/>\n" +
                    "\t\t\t\t<SndFileNme/>\n" +
                    "\t\t\t\t<BgnRec/>\n" +
                    "\t\t\t\t<MaxRec/>\n" +
                    "\t\t\t\t<FileHMac/>\n" +
                    "\t\t\t\t<HMac>3D3121DE740471C1570C6297FF4AAEEF</HMac>\n" +
                    "\t\t\t</RequestHeader>\n" +
                    "\t\t\t<RequestBody>\n" +
                    "\t\t\t\t<OWN>CHWX</OWN>\n" +
                    "\t\t\t\t<BRANCHID>1000</BRANCHID>\n" +
                    "\t\t\t\t<OWNREF>W12020052542065175</OWNREF>\n" +
                    "\t\t\t\t<OWNTASKDESC>登陆</OWNTASKDESC>\n" +
                    "\t\t\t\t<AUTHFLAG>N</AUTHFLAG>\n" +
                    "\t\t\t\t<AUTHORG>SELF</AUTHORG>\n" +
                    "\t\t\t\t<MSGS>\n" +
                    "\t\t\t\t\t<MSG>\n" +
                    "\t\t\t\t\t\t<MSGID>Person01</MSGID>\n" +
                    "\t\t\t\t\t\t<DESC>申请人</DESC>\n" +
                    "\t\t\t\t\t\t<TYPE>Person</TYPE>\n" +
                    "\t\t\t\t\t\t<DATA>\n" +
                    "\t\t\t\t\t\t\t<DATE>2020-05-25</DATE>\n" +
                    "\t\t\t\t\t\t\t<IDS>0635064</IDS>\n" +
                    "\t\t\t\t\t\t</DATA>\n" +
                    "\t\t\t\t\t</MSG>\n" +
                    "\t\t\t\t</MSGS>\n" +
                    "\t\t\t</RequestBody>\n" +
                    "\t\t</cqr:MCR1002>\n" +
                    "\t</soapenv:Body>\n" +
                    "</soapenv:Envelope>";
            JSONObject jsonObject = JSONObject.parseObject(JsonAndXmlUtil.xmlToJson(str)).
                    getJSONObject("soapenv:Envelope").
                    getJSONObject("soapenv:Body").
                    getJSONObject("cqr:MCR1002"). //接口编号，配合金服固定格式
                    getJSONObject("RequestBody");
            System.out.println(jsonObject);
            System.out.println( jsonObject.getJSONObject("MSGS").get("MSG") instanceof JSONArray);

    }


}
