package com.mepeng.cn.SevenPen.account.service.impl;

import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import com.mepeng.cn.SevenPen.account.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("demoService")
public class DemoServiceImpl implements DemoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public Boolean testTcc(Map<String, Object> params) {
        LOGGER.info("account=========testTcc 操作开始================params:{}",params);
        if("123".length()==1){
            //try 方法执行抛出异常时 confirmMethod,cancelMethod 不会执行的
            LOGGER.info("=========account try testTcc 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("account try 操作 模拟确认提交报错123");
        }
        if("123".length()==13){
            //try 方法执行抛出异常时 confirmMethod,cancelMethod 不会执行的
            LOGGER.info("=========account try testTcc 操作 模拟超时====sleep 10s============params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            //throw new HmilyRuntimeException("account try 操作 模拟确认提交报错123");
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("account try testTcc 操作  sleep 10s after ");
        }
        return Boolean.TRUE;
    }

    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    @Override
    public Boolean testTCCFeginAccountTryException(Map<String, Object> params) {
        LOGGER.info("account=========testTCCFeginAccountTryException 操作开始================params:{}",params);
        if("123".length()==3){
            LOGGER.info("=========account try testTCCFeginAccountTryException 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("account  testTCCFeginAccountTryException try 操作 模拟确认提交报错123");
        }
        LOGGER.info("account=========testTCCFeginAccountTryException 操作完成================params:{}",params);
        return Boolean.TRUE;
    }

    public Boolean confirm(Map<String, Object> params) {
        LOGGER.info("account=========testTcc confirm 操作开始================params:{}",params);

        if("123".length()==1){
            //这样确认方法执行抛出异常时 会再调用一次这个方法 直到确认方法没有报错
            LOGGER.info("=========account testTcc confirm 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("account confirm 操作 模拟确认提交报错123");
        }
        LOGGER.info("account=========testTcc confirm 操作完成================params:{}",params);
        return true;
    }
    public Boolean cancel(Map<String, Object> params) {
        LOGGER.info("account=========testTcc cancel 操作开始================params:{}",params);
        if("123".length()==1){
            if(!params.containsKey("failNumAccount")){
                params.put("failNumAccount",1);
            }else{
                int failNum = Integer.parseInt(params.get("failNumAccount").toString());
                System.out.println("account before----------------------failNumAccount:"+failNum+",--params:"+params);
                failNum +=1;
                params.put("failNumAccount",failNum);
                System.out.println("account after----------------------failNumAccount:"+failNum+",--params:"+params);
                if (failNum>3){
                    LOGGER.info("account====直接返回=====cancelTestTCC 操作开始================params:{}",params);
                    return false;
                }
            }
            //这样确认方法执行抛出异常时 会再调用一次这个方法 直到确认方法没有报错
            LOGGER.info("account =========testTcc cancelTestTCC 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("account cancelTestTCC 操作 模拟确认提交报错123");
        }

        LOGGER.info("account=========testTcc cancel 操作完成================params:{}",params);
        return Boolean.TRUE;
    }
}
