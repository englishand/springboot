package com.zhy.netty.server;

import com.zhy.utils.SystemLogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerChannelHandler2 extends ChannelInboundHandlerAdapter {

    private static SystemLogUtil logger  = SystemLogUtil.getLogger();
    private static String logFile = "received.log";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String received = null;
        if (msg instanceof String){
            received = ((String) msg).replaceAll("\r\n","");
            logger.info("服务端处理器2接受到的数据："+received,logFile);
            String result = "|这是服务处理器2返回的数据|";
            ctx.channel().writeAndFlush(result);
        }
        super.channelRead(ctx,msg);
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
