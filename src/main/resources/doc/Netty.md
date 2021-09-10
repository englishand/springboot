#####一、netty线程模型
    1.Reactor单线程模型：指所有的IO操作都在一个NIO线程上完成。NIO线程职责如下：
        作为NIO服务端，接受客户端的TCP连接；作为NIO客户端，向服务端发送TCP连接；读取通讯对端的请求或者应答信息；向通信对端发送信息请求或应答信息；
        由于Reactor模式使用的是异步非阻塞IO，所有的IO操作都不会导致阻塞，理论上一个线程可以独立处理所有的IO相关的操作。从加构从面看，一个NIO线程确
        实可以完成其承担的责任。如：通过Acceptor类接受客户端的TCP连接请求信息，当链路建立成功后，通过Dispatch将对应的ByteBuffer派发到指定的
        Handler上，进行消息解码。线程消息编码后通过NIO线程将消息发送给客户端。但不是适用于高负载、大并发的应用场景。
    2.Reactor多线程模式：专门有一个NIO线程-Acceptor线程用于监听服务端，接收客户端的TCP连接请求；网络IO操作-读写由一个NIO线程池负责，线程池可以采用
        标准的JDK线程池实现，包含一个任务队列和N个可用的线程，由NIO线程负责消息的读取、解码和发送。一个NIO线程可以同时处理N条链路，但一个链路只对应
        一个NIO线程，防止发生并发操作问题。
    ３.netty线程模式：netty的线程模型不是一成不变的，它取决于用户的启动参数配置。通过设置不同的启动参数，Netty可以同时支持Reactor单线程模式、多线程
        模式和主从多线程模型。
一.netty组件：  

    1.Bootstrap or ServerBootstrap,一个Netty应用通常由一个Bootstrap开始，它主要作用是配置整个Netty程序，串起各个组件。
    2.Handler,为了支持各种协议和处理数据的方式，便产生了Handler组件。Handerl主要用来处理各种事件：连接、数据接收、异常、数据转换等。
      ChannelInboundHandler,一个最长用的handler。这个handler的作用就是处理接收到的数据时的事件，即业务逻辑一般写在这个handler中。
    3.ChannelInitializer,当一个连接建立时，ChannelInitailizer用来配置handlers(handler:用来处理接收和发送数据。)，它会提供一个ChannelPipeline,
        并将Handler加入到ChannelPipeline中。
    4.ChannelPipeline：一个Netty应用基于ChannelPipeline机制，这种机制依赖于EventLoop和EventLoopGroup,因为它们都和事件和事件处理相关。
        EventLoop的目的是为Channel处理IO操作，一个EventLoop可以为多个Channel服务。EventLoopGroup会包含多个EventLoop.
    5.Channel:代表了一个socket连接，和EventLoop一起用来参与IO处理。
    6.Future:在Netty中所有的IO操作都是异步的，因此，不能立刻知道消息是否被正确处理，解决方法：通过Future和ChannelFuture,它们可以注册一个监听，
        当操作成功或失败时监听会自动触发。总之，所有的操作都返回一个ChannelFuture.
        channelFuter.channel().closeFuture().sync();作用：Netty server启动，绑定端口开启监听是通过开启一个子线程执行的，当前线程会同步等待；
        closeFuture().sync()就是让当前线程（即主线程）同步等待Netty server的close事件，Netty server的channel close后，主线程才会往下执行。
        closeFuture()在channel close时会通知当前线程。即closeFuture()开启了一个子线程server channel的监听器，负责监听channel是否关闭状态。  
#####二.Netty如何处理连接请求和业务逻辑  
    服务端创建两个NioEventLoopGroup,他们实际上是两个独立的Reactor线程池。一个用于接受客户端的TCP连接，另一个用于处理IO相关的读写操作，或者执行
    系统Task、定时任务Task等。
    用于接收客户端请求的线程池职责如下：1.接收客户端TCP连接，初始化Channel参数；2.将链路状态变更事件通知给ChannelPipeline;
    处理IO操作的线程池职责如下：1.异步读取通讯对端的数据包，发送读事件到ChannelPipeline;2.异步发送消息到通讯对端，调用ChannelPipleline的消息
    发送接口。
    一个Channel会对应一个EventLoop，而一个EventLoop对应一个线程，即仅有一个线程在负责一个Channel的IO操作。
    当一个连接到达，Netty会注册一个channel,然后EventLoopGroup会分配一个EventLoop绑定到这个channel,在这个channel的整生命周期过程中，
    都会由绑定的这个EventLoop来为它服务，而这个EventLoop就是一个线程。
三.Bootstrap和ServerBootstrap  
    
    1.Bootstrap用于客户端，需要调用connect()方法连接服务器端。ServerBootstrap用于服务端，通过调用bind()方法绑定到一个端口监听连接。
    2.客户端的Bootstrap一般用一个EventLoopGroup,而服务器端的ServerBootstrap会用到两个EventLoopGroup,好处：可以把第一个EventLoopGroup专门用来
        负责绑定到端口监听连接事件，而把第二个EventLoopGroup用来处理每个收到的连接。
    ps:如果仅由一个EventLoopGroup处理所有请求和连接，在并发量很大的情况下，这个EventLoopGroup可能会忙于处理已经接收到的连接而不能及时处理新的连接
        请求。  
四.Netty处理数据？Netty核心ChannelHandler
    
    1.ChannelPipeline and Handlers
      数据在ChannelPipeline中流动，这些数据每经过一个ChannelHandler并且被它处理。公共接口ChannelHandler:两个子类接口ChannelInboundHandler和
      ChannelOutboundHandler.一个ChannelPipeline可以把两种handler混合在一起使用，当一个数据流进入ChannelPipeline时，数据会从ChannelPipeline
      头部开始传给第一个ChannelInboundHandler,当第一个处理完后数据会被写出，它会从管道的尾部开始，先经过‘最后’一个ChannelOutboundHander,
      等处理完后再传递给前一个ChannelOutboundHandler.再传递给第二个ChannelInBoundHandler。
      数据在各个handler之间传递，这需要调用方法中传递的ChannelHandlerContext来操作, 在netty的API中提供了两个基类分ChannelOutboundHandlerAdapter
      和ChannelInboundHandlerAdapter.程序中可以继承这两个基类来实现处理数据。
      当一个ChannelHandler被加入到ChannelPipeline中，它便获得一个ChannelHandlerContext的引用，而ChannelHandlerContext可以用来读写netty中
      的数据流。因此现在有两种方式来发送数据：一是把数据直接写入channel，二是把数据写入ChannelHandlerContext,区别：写入Channel的话，数据流会从
      channel的头开始传递，而如果把数据写入ChannelHandlerConteaxt的话，数据流会流入管道中的下一个handler。
      通俗说：ChannelHandlerContext执行写入时使数据在ChannelInboundHandler之间传递.而channel则会传递到所有的outboundHandler.
      例：
      public class InitialierHandler extends ChannelInitializer<SocketChannel> {
          @Override
          protected void initChannel(SocketChannel socketChannel) throws Exception {
            socketChannel.pipeline().addLast(new ResponseChannelHandler1());
            socketChannel.pipeline().addLast(new ResponseChannelHandler2());
            socketChannel.pipeline().addLast(new RequestChannelHandler1());
            socketChannel.pipeline().addLast(new RequestChannelHandler2());
          }
      }
      顺序分别为  in1 →in2 →out2->out1     这儿用图来增加理解 (netty会自动区分in或是out类型)
      图： Socket/Transport                           ChannelPipeline
                 ----------------------->ChannelInboundHandler(in1) ---> ChannelInboundHandler(in2) 
                 <-----------------------ChannelOutboundHandler(out1)<---ChannelOutboundHandler(out2)<----           
                                         头部                                                         尾部
       RequestChannelHandler1的read方法：
       @Override
           public void channelRead(ChannelHandlerContext ctx , Object msg) throws Exception {
               log.info("请求处理器1");
               ctx.writeAndFlush(Unpooled.copiedBuffer("hello word1" , Charset.forName("gb2312")));
               super.channelRead(ctx,msg);
           }
       RequestChannelHandler2的read方法：
       public void channelRead(ChannelHandlerContext ctx , Object msg) throws Exception {
               log.info("请求处理器2");
               super.channelRead(ctx,msg);
           }
       
       
       ResponseChannelHandler1
       public class ResponseChannelHandler1 extends ChannelOutboundHandlerAdapter {
           @Override
           public void write(ChannelHandlerContext ctx , Object msg , ChannelPromise promise) throws Exception {
               log.info("响应处理器1");
               ByteBuf byteMsg = (ByteBuf) msg;
               byteMsg.writeBytes("增加请求1的内容".getBytes(Charset.forName("gb2312")));
               super.write(ctx,msg,promise);
           }
       }
       
       
       ResponseChannelHandler2
       public class ResponseChannelHandler2 extends ChannelOutboundHandlerAdapter {
           @Override
           public void write(ChannelHandlerContext ctx , Object msg , ChannelPromise promise) throws Exception {
               log.info("响应处理器2");
               ByteBuf byteMsg = (ByteBuf) msg;
               byteMsg.writeBytes("增加请求2的内容".getBytes(Charset.forName("gb2312")));
               super.write(ctx,msg,promise);
           }
       }
       调试后发现：没有out1和out2
       
       然后将in2中的ChannelHandlerContext改为Channel后则控制台和网络调试控制台打印分别如下
       public void channelRead(ChannelHandlerContext ctx , Object msg) throws Exception {
               log.info("请求处理器1");
               ctx.channel().writeAndFlush(Unpooled.copiedBuffer("hello word1" , Charset.forName("gb2312")));
               super.channelRead(ctx,msg);
           } 
       结果：两次响应的处理已经打印，并且返回内容已经被分别加上out处理器中的信息     
   五.原码分析  
        
    1.pipeline.addLast()
      通过SocketChannel.pipeline().addLast()添加handler，就是将处理器添加到末尾(netty内部使用一个链表存储)