package com.mepeng.cn.SevenPen.order.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mepeng.cn.SevenPen.order.configuration.RedisMsg;
import org.springframework.stereotype.Component;

/**
 * redis 广播信息接收  RedisMsg.channel1 通道处理
 */
@Component("redisMsg")
public class RedisMsgHandler implements RedisMsg {
    @Override
    public void receiveMessage(Object message) {
        System.out.println("RedisMsgHandler redis 广播信息接收 message:"+message);
        if(message instanceof Person){
            Person person = (Person) message;
            System.out.println("person.id="+person.getId());
            System.out.println("person.name="+person.getName());
            System.out.println("person.crtDate="+person.getCrtDate());
        }
        if(message instanceof String){
            String str = ((String) message).replaceAll("\\\\\"","\"");
            if(str.startsWith("\"")){
                str = str.substring(1,str.length());
            }
            if(str.endsWith("\"")){
                str = str.substring(0,str.length()-1);
            }
            System.out.println("RedisMsgHandler str="+str);
            System.out.println("RedisMsgHandler message="+message);
            JSONObject json = JSONObject.parseObject(str);
            Person person = JSON.toJavaObject(json,Person.class);
            System.out.println("RedisMsgHandler obj person.id="+person.getId());
            System.out.println("RedisMsgHandler obj person.name="+person.getName());
            System.out.println("RedisMsgHandler obj person.crtDate="+person.getCrtDate());

        }
    }

    @Override
    public void receiveMessagePerson(Object message) {

    }
}
