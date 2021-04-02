package com.zhy.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ClientUtils {

    private static Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static void startClient(String host, int port, ChannelHandler... handlers) throws InterruptedException{
        EventLoopGroup workerGroup = new NioEventLoopGroup();//初始化用于连接I/O工作的线程池。
        try {
            Bootstrap b = new Bootstrap();//第1.初始化Bootstrap实例，此实例时netty客户端应用开发的入口。
            b.group(workerGroup);//设置初始化的线程池
            b.channel(NioSocketChannel.class);;//第2.指定通道channel的类型，客户端：NioSocketChannel
            b.option(ChannelOption.SO_KEEPALIVE,true);//设置channelsocket的选项
            //第3.设置socketchannel的处理器，处理读写事件
            b.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nsc) throws Exception {
                    nsc.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                    nsc.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));
                    nsc.pipeline().addLast(new ClientHandler());//找到管道，增加handler
                }
            });
            //连接服务器
            ChannelFuture future = b.connect(host,port).sync();//连接指定的服务器
            //获取通道：NioServerSocketChannel,建立连接，创建客户端channel,发送数据
            future.channel().writeAndFlush(Unpooled.copiedBuffer("这是客户端发送的数据".getBytes()));

            //如果端口连接不上就关闭监听的端口
            future.channel().closeFuture().sync();
//            //接收服务端返回的数据
//            AttributeKey<String> key = AttributeKey.valueOf("ServerData");
//            Object result = future.channel().attr(key).get();
//            logger.info(result.toString());
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
