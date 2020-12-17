package com.zhy.test.utils;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.*;
import java.io.File;

/**
 * 实现文字转语音方法
 * 把jacob-1.18-x64.dll文件复制到jdk安装位置的bin目录下。
 * dll文件下载地址：https://files.cnblogs.com/files/w1441639547/jacob-1.18-x64.rar
 */
@Slf4j
public class TextVoiceTransfer {

    /**
     * 将text转语音
     * @param text
     */
    public static void TextToSpeech(String text){
        ActiveXComponent axc = null;
        try {
            axc = new ActiveXComponent("Sapi.SpVoice");

            //运行时输出语音内容
            Dispatch spVoice = axc.getObject();
            //音量 0-100
            axc.setProperty("Volume",new Variant(100));
            //语音朗读速度 -10 到 10
            axc.setProperty("Rate",new Variant(0));
            //执行朗读
            Dispatch.call(spVoice,"Speak",new Variant(text));

            //构建文件流生成语音文件
            axc = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = axc.getObject();

            axc = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = axc.getObject();

            //设置音频流格式、
            Dispatch.put(spAudioFormat,"Type",new Variant(22));
            //设置文件输出流格式、
            Dispatch.putRef(spFileStream,"Format",spAudioFormat);
            //调用输出文件流打开方法，创建一个.wav文件
            String date = System.currentTimeMillis()+"";
            Dispatch.call(spFileStream,"Open",new Variant("./"+date+".wav"),new Variant(3),new Variant(true));
            //设置声音对象的音频输出流 为 输出文件对象
            Dispatch.putRef(spVoice,"AudioOutputStream",spFileStream);
            //设置音量
            Dispatch.put(spVoice,"Volume",new Variant(100));
            //设置朗读速度
            Dispatch.put(spVoice,"Rate",new Variant(-2));
            //开始朗读
            Dispatch.call(spVoice,"Speak",new Variant(text));

            //关闭输出文件
            Dispatch.call(spFileStream,"Close");
            Dispatch.putRef(spVoice,"AudioOutputStream",null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();
            spVoice.safeRelease();
            axc.safeRelease();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 播放语音文件
     * @param fileName
     */
    public static void FileWithSpeech(String fileName){
        AudioInputStream as;
        SourceDataLine sdl = null;
        try {
            as = AudioSystem.getAudioInputStream(new File("./zhy20201217.wav"));
            AudioFormat format = as.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class,format);
            sdl = (SourceDataLine) AudioSystem.getLine(info);
            sdl.open(format);
            sdl.start();

            int nBytesRead = 0;
            byte[] abData = new byte[512];
            while (nBytesRead!=-1){
                nBytesRead = as.read(abData,0,abData.length);
                if (nBytesRead>=0){
                    sdl.write(abData,0,nBytesRead);
                }
            }
            //关闭Sourcedataline
            sdl.drain();
            sdl.close();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    public static void main(String[] args){
        TextVoiceTransfer transfer = new TextVoiceTransfer();
        transfer.TextToSpeech("应急公益短信：蠕动易来山东不利于空气质量的气象条件多发。国家航天局副局长、探月工程副总指挥吴艳华在12月17日下午的国新办发布会上表示，嫦娥五号任务是我国复杂度最高、技术跨度最大的航天系统工程，首次实现了我国地外天体采样返回。");

        transfer.FileWithSpeech("./zhy20201217.wav");
    }

}
