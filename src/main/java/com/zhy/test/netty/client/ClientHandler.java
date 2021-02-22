package com.zhy.test.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

import java.nio.charset.Charset;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String value = "";
        if (msg instanceof ByteBuf){
            value = ((ByteBuf)msg).toString(Charset.forName("GBK"));
        }else if (msg instanceof String){
            value = (String) msg;
        }
        System.out.println("服务器端返回的数据："+value);

        AttributeKey<String> key = AttributeKey.valueOf("ServerData");
        ctx.channel().attr(key).set("客户端处理完毕");

        //客户端通道关闭
        ctx.channel().close();
    }
}
