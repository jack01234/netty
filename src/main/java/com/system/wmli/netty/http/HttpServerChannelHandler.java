package com.system.wmli.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.springframework.http.HttpHeaders;

/**
 * http 服务端处理类
 *
 * @author yingmuhuadao
 * @date 2019/6/25
 */
public class HttpServerChannelHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        ctx.channel().remoteAddress();
        FullHttpRequest request = msg;

        System.out.println("请求方法名称:" + request.getMethod().name());

        System.out.println("uri:" + request.getUri());
        ByteBuf buf = request.content();
        System.out.print(buf.toString(CharsetUtil.UTF_8));


        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        response.headers().add(HttpHeaders.CONTENT_TYPE, "text/plain");
        response.headers().add(HttpHeaders.CONTENT_LENGTH, byteBuf.readableBytes());

        ctx.writeAndFlush(response);
    }
}
