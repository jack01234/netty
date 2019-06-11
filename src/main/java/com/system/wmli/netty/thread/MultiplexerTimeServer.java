package com.system.wmli.netty.thread;

import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.Set;

/**
 * 多路复用类
 *
 * @author yingmuhuadao
 * @date 2019/6/10
 */
public class MultiplexerTimeServer implements Runnable{

    private Selector selector;

    private ServerSocketChannel socketChannel;

    private volatile boolean stop;

    /**
     * 初始化多路复用器，绑定监听端口
     */
    public MultiplexerTimeServer(int port){
        try {
            selector = Selector.open();
            socketChannel = ServerSocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.socket().bind(new InetSocketAddress(port),1024);
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The time server is start in port : "+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleInput(SelectionKey key) throws IOException {
            if (key.isValid()) {
                //处理新接入的请求消息
                if (key.isAcceptable()) {
                    //accept new connection
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    SocketChannel sc = ssc.accept();
                    // add the new connection to the selector
                    sc.register(selector, SelectionKey.OP_ACCEPT);
                }

                if (key.isReadable()) {
                    //read the data
                    SocketChannel sc = (SocketChannel) key.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int readBytes = sc.read(readBuffer);
                    if (readBytes > 0) {
                        readBuffer.flip();
                        byte [] bytes = new byte[readBuffer.remaining()];
                        readBuffer.get(bytes);
                        String body = new String(bytes,"UTF-8");
                        System.out.println("The time server receive order : "+body);
                        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
                                new Date(System.currentTimeMillis()).toString():"BAD ORDER";

                        doWrite(sc, currentTime);
                    } else if (readBytes < 0 ){
                        //对端链路关闭
                        key.cancel();
                        sc.close();
                    } else {
                        //读到0字节，忽略了
                    }
                }
            }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        if (!StringUtils.isEmpty(response)) {
            byte[] bytes = response.getBytes();
            ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
            allocate.put(bytes);
            allocate.flip();
            channel.write(allocate);
        }
    }
}
