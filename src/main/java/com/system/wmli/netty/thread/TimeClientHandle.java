package com.system.wmli.netty.thread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 客户端处理器
 *
 * @author yingmuhuadao
 * @date 2019/6/10
 */
public class TimeClientHandle implements Runnable{

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;


    public TimeClientHandle(String host, int port){
        this.host = host == null?"127.0.0.1" :host;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try {
                        handleInput(key);
                    } catch (IOException e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel()!=null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doConnect() throws IOException {
        //如果直接连接成功，则注册到多路复用器上，发送请求信息， 读应答
        if (socketChannel.connect(new InetSocketAddress(host,port))) {
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            doWrite(socketChannel);

        } else {
           socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte [] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer allocate = ByteBuffer.allocate(req.length);
        allocate.put(req);
        allocate.flip();
        sc.write(allocate);
        if (!allocate.hasRemaining()) {
            System.out.println("Send order 2 server success");
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //判断连接是否成功
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);
                    doWrite(sc);
                }
            } else {
                //连接失败
                System.exit(1);
            }

            if (key.isReadable()) {
                ByteBuffer allocate = ByteBuffer.allocate(1024);
                int read = sc.read(allocate);
                if (read > 0) {
                    allocate.flip();
                    byte[] bytes = new byte[allocate.remaining()];
                    ByteBuffer byteBuffer = allocate.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("Now is : "+body);
                    this.stop = true;
                } else if (read < 0){
                    //对端链路关闭
                    key.channel();
                    sc.close();
                } else {
                    //读到0字节忽略
                }
            }
        }
    }
}
