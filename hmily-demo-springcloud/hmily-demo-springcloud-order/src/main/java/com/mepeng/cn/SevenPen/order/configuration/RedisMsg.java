package com.mepeng.cn.SevenPen.order.configuration;

//import org.springframework.stereotype.Component;

/**
 *  定义接受信息的接口
 */
public interface RedisMsg {
    /**
     * 广播通道
     */
    public static String channel = "publish_channel_no_1";

    public static String channel2 = "publish_channel_no_2";

    /**
     * 接受信息
     * @param message
     */
    public void receiveMessage(Object message);

    public void receiveMessagePerson(Object message);
}
