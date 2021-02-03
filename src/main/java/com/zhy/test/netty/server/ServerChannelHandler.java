package com.zhy.test.netty.server;

import com.zhy.test.utils.SystemLogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static SystemLogUtil logger  = SystemLogUtil.getLogger();
    private static String logFile = "received.log";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String received = null;
        String result = "测试netty server返回信息";
        if (msg instanceof String){
            received = (String)msg;
            received = received.replaceAll("\r\n","");
            logger.info(received,logFile);
            ctx.writeAndFlush(received);
        }else {
            ByteBuf buf = (ByteBuf)msg;
            byte[] array = new byte[buf.readableBytes()];
                buf.getBytes(0,array);

            received = new String(array,"GBK");
            System.out.println(received);
            buf.release();

            logger.info("返回响应报文："+result,logFile);
            byte[] response = (String.format(result, Charset.defaultCharset())).getBytes();
            ByteBuf bb = Unpooled.buffer();
            bb.writeBytes(response);
            ctx.channel().writeAndFlush(bb);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
