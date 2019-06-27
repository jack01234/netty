package com.system.wmli.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.springframework.http.HttpHeaders;

import java.net.URI;

/**
 * 服务端连处理类
 *
 * @author yingmuhuadao
 * @date 2019/6/25
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, FullHttpResponse msg) throws Exception {
        FullHttpResponse response = msg;
        response.headers().get(HttpHeaders.CONTENT_TYPE);
        ByteBuf buf = response.content();
        System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        URI uri = new URI("http://127.0.0.1:8080");
        String msg = "Are you ok?";
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

        // 构建http请求
//        request.headers().set(HttpHeaderNames.HOST, "127.0.0.1");
//        request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        request.headers().set(HttpHeaders.CONTENT_LENGTH, request.content().readableBytes());
        // 发送http请求
        ctx.channel().writeAndFlush(request);
    }
}
