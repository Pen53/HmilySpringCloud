package com.mepeng.cn.SevenPen.order.kryo;

public interface ObjectSerializer {
    byte[] serialize(Object obj) throws RuntimeException;

    <T> T deSerialize(byte[] param, Class<T> clazz) throws RuntimeException;
}
