package com.system.wmli.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * netty
 * 时间客户端
 *
 * @author yingmuhuadao
 * @date 2019/6/11
 */
public class TimeClient {

    private void connect(int port, String host) throws InterruptedException {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap s = new Bootstrap();
            s.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().
                                    addLast(new LineBasedFrameDecoder(1024));
                                    socketChannel.pipeline().addLast(new StringDecoder());
                                    socketChannel.pipeline().
                                    addLast(new TimeClientHandler());
                        }
                    });
            //发起异步连接操作
            ChannelFuture f = s.connect(host, port).sync();

            //等待客户端链路关闭
            f.channel().closeFuture().sync();

        } finally {
            //优雅退出，释放NIO资源
            group.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        var port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        new TimeClient().connect(port,"127.0.0.1");
    }
}
