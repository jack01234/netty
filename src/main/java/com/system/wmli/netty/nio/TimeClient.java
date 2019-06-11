package com.system.wmli.netty.nio;

import com.system.wmli.netty.thread.TimeClientHandle;

/**
 * NIO
 * @author yingmuhuadao
 * @date 2019/6/10
 */
public class TimeClient {
    public static void main(String[] args) {
        var port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeClient-001").start();
    }
}
