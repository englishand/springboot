package com.zhy.netty.server;

import com.zhy.utils.SystemLogUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
@Slf4j
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    private static SystemLogUtil logger  = SystemLogUtil.getLogger();
    private static String logFile = "received.log";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String received = null;
        String result = "这是服务处理器1返回的数据|";
        if (msg instanceof String){
            received = (String)msg;
            received = received.replaceAll("\r\n","");
            logger.info("服务器处理器1接收到的数据："+received,logFile);
//            ctx.channel().writeAndFlush(result);//将数据传递到ChannelOutboundHandler

            ctx.fireChannelRead(result);//使数据在ChannelInboundHandler之间传递
        }else {
            ByteBuf buf = (ByteBuf)msg;
            byte[] array = new byte[buf.readableBytes()];
                buf.getBytes(0,array);

            received = new String(array,"GBK");
            log.info(received);
            buf.release();

            logger.info("返回响应报文："+result,logFile);
            byte[] response = (String.format(result, Charset.defaultCharset())).getBytes();
            ByteBuf bb = Unpooled.buffer();
            bb.writeBytes(response);
            ctx.writeAndFlush(bb);

            //或用以下方法发送
//            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(result,Charset.forName("UTF-8")));
        }
//        super.channelRead(ctx,msg);//使多个ChannelInboundHandler重复从channel头执行，所以多个inhandler接收到的信息收到多个信息，所以应当注掉
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
