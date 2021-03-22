package com.zhy.test.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.zhy.test.utils.ZXingCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@Controller
@RequestMapping("qr")
public class QrController {

    @RequestMapping("/toQrCode")
    public String toQrcode(){
//        ModelAndView mv  = new ModelAndView("/qrcode/qrCode");
        return "/qrcode/qrCode";
    }

    /**
     * 方法一：直接生成图片，并用ResponseEntity返回，无法传递参数
     * @return
     */
    @RequestMapping("qrImage")
    public ResponseEntity<byte[]> getQrImage(){
        //二维码内信息
        String info = "this is my first qrimage";

        byte[] qrcode = null;
        try {
            qrcode = getQRCodeImage(info,360,360);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set header
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(qrcode,headers, HttpStatus.CREATED);
    }

    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }

    /**
     * 方法二：生成二维码图片，并用ResponseEntity返回，可传递参数
     * @return
     */
    @RequestMapping("qrImage2")
    public void getQrImage2(String content,Integer size, HttpServletRequest request, HttpServletResponse response){

        if (StringUtils.isNotBlank(content)){
            if (size==null){
                size=100;
            }
            response.setContentType("image/png");
            try{
                ZXingCode zp = ZXingCode.getInstance();
                BufferedImage bim = zp.getQRCODEBufferedImage(content,BarcodeFormat.QR_CODE,size,size,zp.getDecodeHintType());
                ImageIO.write(bim,"png",response.getOutputStream());
            }catch (Exception e){
                log.info(e.getMessage());
            }
        }
    }

}
