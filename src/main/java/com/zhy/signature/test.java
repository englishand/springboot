package com.zhy.signature;

import com.zhy.signature.sign.SignUtil;
import com.zhy.signature.verifySign.VerifySign;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {

    public static void main(String[] args){
        String paramXml = "\"<root><MsgHeader><MsgVer>1000</MsgVer><SndDt>2020-11-24T16:51:04</SndDt><Trxtyp>0001</Trxtyp><IssrId>00010000</IssrId><Drctn>11</Drctn><SignSN>4002486073</SignSN><EncSN/><EncKey/><MDAlgo>0</MDAlgo><SignEncAlgo>0</SignEncAlgo><EncAlgo/></MsgHeader><MsgBody><BizTp>300001</BizTp><BizFunc>111011</BizFunc><BizAssInf><BizAssInfRsv/></BizAssInf><TrxInf><TrxId>1124160620786420</TrxId><TrxDtTm>2020-11-24T16:51:04</TrxDtTm><SettlmtDt>2020-11-24</SettlmtDt><SettlmtInf>1423</SettlmtInf><TrxAmt>CNY100.00</TrxAmt></TrxInf><RcverInf><RcverAcctIssrId>04429201</RcverAcctIssrId><RcverAcctId>6251190010000307</RcverAcctId><RcverAcctTp/><RcverNm>美十六</RcverNm><IDTp>01</IDTp><IDNo>411522199010280012</IDNo><MobNo>13716543069</MobNo></RcverInf><SensInf/><SderInf><SderIssrId>meituan</SderIssrId><SderAcctIssrId>48179202</SderAcctIssrId><SderAcctIssrNm>美团无卡快捷支付</SderAcctIssrNm></SderInf><CorpCard><CorpName/><USCCode/></CorpCard><ProductInf><ProductTp>00000000</ProductTp><ProductAssInformation>一般购物消费</ProductAssInformation></ProductInf><MrchntInf><MrchntNo>MrchntNo0000001</MrchntNo><MrchntTpId>5411</MrchntTpId><MrchntPltfrmNm>无卡快捷支付测试商户Test_Mchnt</MrchntPltfrmNm></MrchntInf><SubMrchntInf><SubMrchntNo/><SubMrchntTpId/><SubMrchntPltfrmNm/></SubMrchntInf><RskInf><deviceMode/><deviceLanguage/><sourceIP/><MAC/><devId/><extensiveDeviceLocation/><deviceNumber/><deviceSIMNumber/><accountIDHash/><riskScore/><riskReasonCode/><mchntUsrRgstrTm/><mchntUsrRgstrEmail/><rcvProvince/><rcvCity/><goodsClass/></RskInf></MsgBody></root>\"";

        paramXml = paramXml.replaceAll("\n","").replaceAll("\t","");
        String signature = null;
        String keyFile = "jks/4002486073.pfx";
        String storepass = "123456";
        String keypass = "123456";
        String storetype = "PKCS12";
        String alias = "unionpay";
        try {
            signature = SignUtil.sign(paramXml.getBytes(),keyFile,alias,keypass,storepass,storetype);
            log.info("已签名数据："+signature);
        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("***************************");
        log.info("***************************");
        boolean result = VerifySign.verifySign(paramXml,signature,keyFile,storepass,keypass,alias);
        log.info("验签结果:{}",result);
    }
}
