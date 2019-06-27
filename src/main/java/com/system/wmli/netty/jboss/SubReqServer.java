//package com.system.wmli.netty.jboss;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.ServerSocketChannel;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import io.netty.handler.codec.marshalling.MarshallingDecoder;
//import io.netty.handler.logging.LogLevel;
//import io.netty.handler.logging.LoggingHandler;
//import org.jboss.marshalling.MarshallerFactory;
//
///**
// * jboss 图书订购
// *
// * @author yingmuhuadao
// * @date 2019/6/13
// */
//public class SubReqServer {
//
//    public void bind(int port){
//        //配置服务端的NIO线程组
//        EventLoopGroup bossGroup = new NioEventLoopGroup();
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//
//        ServerBootstrap b = new ServerBootstrap();
//        b.group(bossGroup, workerGroup)
//                .channel(ServerSocketChannel.class)
//                .option(ChannelOption.SO_BACKLOG, 100)
//                .handler(new LoggingHandler(LogLevel.INFO))
//                .childHandler(new ChannelInitializer<SocketChannel>() {
//                    @Override
//                    protected void initChannel(SocketChannel socketChannel) throws Exception {
//                        socketChannel.pipeline().addLast(MarshallingC)
//                                .
//                    }
//                });
//
//    }
//
//
//    public final class MarshallingCodeFactory{
//        public static MarshallingDecoder buildMarshallingDecoder(){
//
//        }
//    }
//}
