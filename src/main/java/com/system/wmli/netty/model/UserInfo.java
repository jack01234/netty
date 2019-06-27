package com.system.wmli.netty.model;

/**
 * @author yingmuhuadao
 * @date 2019/6/12
 */

public class UserInfo {

    private String name;

    private String address;

    private int age;


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
