package com.system.wmli.netty.netty;

import com.system.wmli.netty.model.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author yingmuhuadao
 * @date 2019/6/12
 */
public class EchoClientHandler extends ChannelHandlerAdapter {

    private final int sendNum;

    public EchoClientHandler(int sendNum){
        this.sendNum = sendNum;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos = UserInfo();
        for (UserInfo userInfo:userInfos) {
            ctx.write(userInfo);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message : "+msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private UserInfo[] UserInfo(){
        UserInfo [] userInfos = new UserInfo[sendNum];
        UserInfo userInfo;
        for (int i=0; i<sendNum; i++) {
            userInfo = new UserInfo();
            userInfo.setAge(i);
            userInfo.setName("ABCDEFG --->"+i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }
}
