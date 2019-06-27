package com.system.wmli.netty.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * msgpack编解码测试
 *
 * @author yingmuhuadao
 * @date 2019/6/12
 */
public class Test {

    public static void main(String[] args) throws IOException {
        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        MessagePack pack = new MessagePack();
        //序列化
        byte[] write = pack.write(list);
        //反序列化
        List<String> read = pack.read(write, Templates.tList(Templates.TString));
        System.out.println(read.get(0));
    }
}
