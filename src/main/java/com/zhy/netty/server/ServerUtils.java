package com.zhy.netty.server;

import com.zhy.utils.SystemLogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;

public class ServerUtils {

    private static SystemLogUtil logger = SystemLogUtil.getLogger();
    private static String errorLog = "error.log";

    public static void startServer(int port, ChannelHandler... handlers) throws InterruptedException{
        //第1.初始化用于Acceptor的主”线程池“以及用于I/O工作的从”线程池”，用来处理客户端通道的accept和读写事件；
        EventLoopGroup bossGroup = new NioEventLoopGroup();//用来处理accept事件。获取客户端连接，连接接收到后再将连接转发给childgroup处理。
        EventLoopGroup workerGroup = new NioEventLoopGroup();//用来处理通道的读写事件
        try {
            //初始化ServerBootstrap实例,启动服务器，此实例是netty服务端应用开发的入口
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup,workerGroup)//通过group方法，设置初始化的主从“线程池”
                .channel(NioServerSocketChannel.class)//第2.指定通道channel的类型，服务端：NioServerSocketChannel
                .childHandler(new ChannelInitializer<SocketChannel>() {//第3.设置子通道即SocketChannel的处理器。处理读写事件。ChannelInitializer是给通道初始化
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new StringDecoder(Charset.forName("UTF-8")));
                        sc.pipeline().addLast(new StringEncoder(Charset.forName("UTF-8")));



                        sc.pipeline().addLast(new ServerChannelHandler());//添加处理器

                        sc.pipeline().addLast(new ServerChannelOutHandler());
                        sc.pipeline().addLast(new ServerChannelHandler2());


                        sc.pipeline().addLast(new ServerChannelOutHandler2());

                    }
                })
                .option(ChannelOption.SO_BACKLOG,128)//配置ServerSocketChannel的选项
            ;
            ChannelFuture cf  = b.bind(port).sync();//第4.绑定并监听端口，代码低层就是server.accept()建立连接，因为accept()是阻塞的，所以是异步
            cf.channel().closeFuture().sync();//如果端口连接不上就关闭监听的端口
        }catch (Exception e){
            logger.error("netty服务器启动类异常",errorLog,e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}
