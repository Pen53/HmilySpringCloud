package com.mepeng.cn.SevenPen.order.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * redis发布订阅配置
 */
@Configuration
public class RedisPublishConfig {

    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            @Qualifier("listenerAdapter")MessageListenerAdapter listenerAdapter,
                                            @Qualifier("listenerAdapter2")MessageListenerAdapter listenerAdapter2,
                                            @Qualifier("listenerAdapter3")MessageListenerAdapter listenerAdapter3) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 订阅通道
        container.addMessageListener(listenerAdapter, new PatternTopic(RedisMsg.channel));
        container.addMessageListener(listenerAdapter2, new PatternTopic(RedisMsg.channel2));
        container.addMessageListener(listenerAdapter3, new PatternTopic(RedisMsg.channel2));
        // 这个container 可以添加多个 messageListener
        return container;
    }
    /**
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     *
     * @param receiver
     * @return
     */
    @Bean("listenerAdapter")
    MessageListenerAdapter listenerAdapter(@Qualifier("redisMsg") RedisMsg receiver) {
        // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        // 也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }


    @Bean("listenerAdapter2")
    MessageListenerAdapter listenerAdapter2(@Qualifier("redisMsg2") RedisMsg receiver) {
        // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        // 也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    @Bean("listenerAdapter3")
    MessageListenerAdapter listenerAdapter3(@Qualifier("redisMsg2") RedisMsg receiver) {
        // 这个地方 是给messageListenerAdapter 传入一个消息接受的处理器，利用反射的方法调用“receiveMessage”
        // 也有好几个重载方法，这边默认调用处理器的方法 叫handleMessage
        return new MessageListenerAdapter(receiver, "receiveMessagePerson");
    }

}
