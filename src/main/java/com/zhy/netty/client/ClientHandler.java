package com.zhy.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private Logger log = LoggerFactory.getLogger(ClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String value = "";
        if (msg instanceof ByteBuf){
            value = ((ByteBuf)msg).toString(Charset.forName("GBK"));
        }else if (msg instanceof String){
            value = (String) msg;
        }
        log.info("服务器端返回的数据："+value);

//        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
//        ctx.channel().attr(key).set(value);

        //客户端通道关闭
        ctx.channel().close();
    }
}
