package com.system.wmli.netty.nio;

import com.system.wmli.netty.thread.MultiplexerTimeServer;
import com.system.wmli.netty.thread.TimeClientHandle;

/**
 * NIO
 *
 * @author yingmuhuadao
 * @date 2019/6/10
 */
public class TimeServer {
    public static void main(String[] args) {
        var port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
            }
        }
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"timeServer-001").start();
    }
}
