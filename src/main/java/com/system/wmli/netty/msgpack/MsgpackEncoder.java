package com.system.wmli.netty.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * 编码器
 * @author yingmuhuadao
 * @date 2019/6/12
 */
public class MsgpackEncoder extends MessageToByteEncoder<Object> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object byteBuf, ByteBuf byteBuf2) throws Exception {
        MessagePack pack = new MessagePack();
        byte[] write = pack.write(byteBuf);
        byteBuf2.writeBytes(write);
    }
}
