package com.mepeng.cn.SevenPen.order.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mepeng.cn.SevenPen.order.configuration.RedisMsg;
import org.springframework.stereotype.Component;

/**
 * redis 广播信息接收 RedisMsg.channel2 通道处理
 */
@Component("redisMsg2")
public class RedisMsgHandler2 implements RedisMsg {
    @Override
    public void receiveMessage(Object message) {
        System.out.println("RedisMsgHandler2 redis 广播信息接收 message:"+message);
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
            System.out.println("RedisMsgHandler2 str="+str);
            System.out.println("RedisMsgHandler2 message="+message);
            JSONObject json = JSONObject.parseObject(str);
            Person person = JSON.toJavaObject(json,Person.class);
            System.out.println("RedisMsgHandler2 obj person.id="+person.getId());
            System.out.println("RedisMsgHandler2 obj person.name="+person.getName());
            System.out.println("RedisMsgHandler2 obj person.crtDate="+person.getCrtDate());

        }
    }

    @Override
    public void receiveMessagePerson(Object message) {
        System.out.println("RedisMsgHandler2 receiveMessagePerson redis 广播信息接收 message:"+message);
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
            System.out.println("RedisMsgHandler2 receiveMessagePerson str="+str);
            System.out.println("RedisMsgHandler2 receiveMessagePerson message="+message);
            JSONObject json = JSONObject.parseObject(str);
            Person person = JSON.toJavaObject(json,Person.class);
            System.out.println("RedisMsgHandler2 receiveMessagePerson obj person.id="+person.getId());
            System.out.println("RedisMsgHandler2 receiveMessagePerson obj person.name="+person.getName());
            System.out.println("RedisMsgHandler2 receiveMessagePerson obj person.crtDate="+person.getCrtDate());

        }
    }
}
