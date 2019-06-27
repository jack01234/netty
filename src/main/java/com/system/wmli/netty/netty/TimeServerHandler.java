package com.system.wmli.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 *
 * 时间服务处理类
 * @author yingmuhuadao
 * @date 2019/6/11
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    /**
     * 计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String buf = (String) msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        String body = new String(req,"UTF-8");
        System.out.println("The time server receive order : "+buf+"; the counter is "+ ++counter);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(buf)?
                new Date(System.currentTimeMillis()).toString():"BAD ORDER";
        currentTime = currentTime+System.getProperty("line.separator");
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.write(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }



}
