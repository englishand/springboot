package com.zhy.test.signature;

import com.zhy.test.signature.sign.SignUtil;
import com.zhy.test.signature.verifySign.VerifySign;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class test {

    public static void main(String[] args){
        String paramXml = "<root>\n" +
                "\t<MsgHeader>\n" +
                "\t\t<MsgVer>1000</MsgVer>\n" +
                "\t\t<SndDt>2020-11-24T16:51:04</SndDt>\n" +
                "\t\t<Trxtyp>0001</Trxtyp>\n" +
                "\t\t<IssrId>00010000</IssrId>\n" +
                "\t\t<Drctn>11</Drctn>\n" +
                "\t\t<SignSN>4002486073</SignSN>\n" +
                "\t\t<EncSN/>\n" +
                "\t\t<EncKey/>\n" +
                "\t\t<MDAlgo>0</MDAlgo>\n" +
                "\t\t<SignEncAlgo>0</SignEncAlgo>\n" +
                "\t\t<EncAlgo/>\n" +
                "\t</MsgHeader>\n" +
                "\t<MsgBody>\n" +
                "\t\t<BizTp>300001</BizTp>\n" +
                "\t\t<BizFunc>111011</BizFunc>\n" +
                "\t\t<BizAssInf>\n" +
                "\t\t\t<BizAssInfRsv/>\n" +
                "\t\t</BizAssInf>\n" +
                "\t\t<TrxInf>\n" +
                "\t\t\t<TrxId>1124160620786420</TrxId>\n" +
                "\t\t\t<TrxDtTm>2020-11-24T16:51:04</TrxDtTm>\n" +
                "\t\t\t<SettlmtDt>2020-11-24</SettlmtDt>\n" +
                "\t\t\t<SettlmtInf>1423</SettlmtInf>\n" +
                "\t\t\t<TrxAmt>CNY100.00</TrxAmt>\n" +
                "\t\t</TrxInf>\n" +
                "\t\t<RcverInf>\n" +
                "\t\t\t<RcverAcctIssrId>04429201</RcverAcctIssrId>\n" +
                "\t\t\t<RcverAcctId>6251190010000307</RcverAcctId>\n" +
                "\t\t\t<RcverAcctTp/>\n" +
                "\t\t\t<RcverNm>美十六</RcverNm>\n" +
                "\t\t\t<IDTp>01</IDTp>\n" +
                "\t\t\t<IDNo>411522199010280012</IDNo>\n" +
                "\t\t\t<MobNo>13716543069</MobNo>\n" +
                "\t\t</RcverInf>\n" +
                "\t\t<SensInf/>\n" +
                "\t\t<SderInf>\n" +
                "\t\t\t<SderIssrId>meituan</SderIssrId>\n" +
                "\t\t\t<SderAcctIssrId>48179202</SderAcctIssrId>\n" +
                "\t\t\t<SderAcctIssrNm>美团无卡快捷支付</SderAcctIssrNm>\n" +
                "\t\t</SderInf>\n" +
                "\t\t<CorpCard>\n" +
                "\t\t\t<CorpName/>\n" +
                "\t\t\t<USCCode/>\n" +
                "\t\t</CorpCard>\n" +
                "\t\t<ProductInf>\n" +
                "\t\t\t<ProductTp>00000000</ProductTp>\n" +
                "\t\t\t<ProductAssInformation>一般购物消费</ProductAssInformation>\n" +
                "\t\t</ProductInf>\n" +
                "\t\t<MrchntInf>\n" +
                "\t\t\t<MrchntNo>MrchntNo0000001</MrchntNo>\n" +
                "\t\t\t<MrchntTpId>5411</MrchntTpId>\n" +
                "\t\t\t<MrchntPltfrmNm>无卡快捷支付测试商户Test_Mchnt</MrchntPltfrmNm>\n" +
                "\t\t</MrchntInf>\n" +
                "\t\t<SubMrchntInf>\n" +
                "\t\t\t<SubMrchntNo/>\n" +
                "\t\t\t<SubMrchntTpId/>\n" +
                "\t\t\t<SubMrchntPltfrmNm/>\n" +
                "\t\t</SubMrchntInf>\n" +
                "\t\t<RskInf>\n" +
                "\t\t\t<deviceMode/>\n" +
                "\t\t\t<deviceLanguage/>\n" +
                "\t\t\t<sourceIP/>\n" +
                "\t\t\t<MAC/>\n" +
                "\t\t\t<devId/>\n" +
                "\t\t\t<extensiveDeviceLocation/>\n" +
                "\t\t\t<deviceNumber/>\n" +
                "\t\t\t<deviceSIMNumber/>\n" +
                "\t\t\t<accountIDHash/>\n" +
                "\t\t\t<riskScore/>\n" +
                "\t\t\t<riskReasonCode/>\n" +
                "\t\t\t<mchntUsrRgstrTm/>\n" +
                "\t\t\t<mchntUsrRgstrEmail/>\n" +
                "\t\t\t<rcvProvince/>\n" +
                "\t\t\t<rcvCity/>\n" +
                "\t\t\t<goodsClass/>\n" +
                "\t\t</RskInf>\n" +
                "\t</MsgBody>\n" +
                "</root>";

        paramXml = paramXml.replaceAll("\n","").replaceAll("\t","");
        String signature = null;

        try {
            signature = SignUtil.sign(paramXml.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("***************************");
        boolean result = VerifySign.verifySign(paramXml,signature);
        log.info("验签结果:{}",result);
    }
}
