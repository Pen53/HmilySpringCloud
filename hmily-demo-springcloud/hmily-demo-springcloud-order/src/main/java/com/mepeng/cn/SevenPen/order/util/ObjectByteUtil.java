package com.mepeng.cn.SevenPen.order.util;

import java.io.*;

/**
 * 对象与二进制 转换工具类
 */
public class ObjectByteUtil {
    public static byte[] objToBytes(Object obj) throws IOException {
        ByteArrayOutputStream byt=new ByteArrayOutputStream();
        ObjectOutputStream objOs=new ObjectOutputStream(byt);
        objOs.writeObject(obj);
        byte[] bytes=byt.toByteArray();
        System.out.println(bytes);
        return bytes;
    }

    public static Object byteToObject(byte[]  bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInt=new ByteArrayInputStream(bytes);
        ObjectInputStream objInt=new ObjectInputStream(byteInt);
        Object obj= objInt.readObject();
        System.out.println(obj);
        return obj;
    }
}
