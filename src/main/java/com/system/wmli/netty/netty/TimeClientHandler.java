package com.system.wmli.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * 客户端处理
 *
 * @author yingmuhuadao
 * @date 2019/6/12
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

//    private final ByteBuf firstMessage;

    private byte[] req;

    private int counter;


    public TimeClientHandler(){
        req = "QUERY TIME ORDER".getBytes();
//        firstMessage = Unpooled.buffer(bytes.length);
//        firstMessage.writeBytes(bytes);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        System.out.println("释放资源"+cause);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message;
        for (int i=0; i<100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
        String body = (String) msg;
        System.out.println("Now is : "+body+"; the counter is "+ ++counter);
    }
}
