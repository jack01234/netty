package com.system.wmli.netty.netty;

import com.system.wmli.netty.msgpack.MsgpackDecoder;
import com.system.wmli.netty.msgpack.MsgpackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 测试
 * @author yingmuhuadao
 * @date 2019/6/13
 */
public class EchoServerV1 {

    public void run() throws InterruptedException {
        EventLoopGroup acceptorGroup = new NioEventLoopGroup();
        EventLoopGroup IOGroup = new NioEventLoopGroup();


        try {
            ServerBootstrap s = new ServerBootstrap();
            s.group(acceptorGroup,IOGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast("msgpack decoder",new MsgpackDecoder());
                            socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            //绑定端口，同步等待成功
            ChannelFuture f = s.bind(8080).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } finally {
            acceptorGroup.shutdownGracefully();
            IOGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new EchoServerV1().run();
    }
}
