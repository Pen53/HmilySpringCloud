package com.mepeng.cn.SevenPen.order.kryo.Dto;

import lombok.Data;

@Data
public class SubTestSerialization {
    private String name;

    public static void main(String[] args) {
        SubTestSerialization test = new SubTestSerialization();
        test.setName("zs");
        System.out.println("test1:"+test);
    }
}
