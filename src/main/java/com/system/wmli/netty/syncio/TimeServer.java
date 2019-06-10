package com.system.wmli.netty.syncio;

import com.system.wmli.netty.thread.TimeServerHandler;
import com.system.wmli.netty.thread.TimeServerHandlerExecutePool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 同步阻塞式I/O
 *
 * @author yingmuhuadao
 * @date 2019/6/9
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if (args != null && args.length >0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e){
                //采用默认值
            }
        }
        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.out.println("The time server is start in port : "+port);
            Socket socket = null;
            while (true){
                socket = server.accept();
                /**
                 * 同步IO，每次任务分配一个线程
                 */
//                new Thread(new TimeServerHandler(socket)).start();
                /**
                 * 创建线程池
                 */
                TimeServerHandlerExecutePool pool = new TimeServerHandlerExecutePool(50,100);
                while (true) {
                    socket = server.accept();
                    pool.execute(new TimeServerHandler(socket));
                }
            }
        } finally {
            if (server != null) {
                System.out.println("The time Server close");
                server.close();
                server = null;
            }
        }
    }
}
