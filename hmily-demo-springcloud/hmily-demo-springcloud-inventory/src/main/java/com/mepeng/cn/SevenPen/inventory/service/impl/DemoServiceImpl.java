package com.mepeng.cn.SevenPen.inventory.service.impl;

import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import com.mepeng.cn.SevenPen.inventory.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("demoService")
public class DemoServiceImpl implements DemoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);
    @Override
    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    public boolean testTcc(Map<String, Object> params) {
        LOGGER.info("inventory=========testTcc 操作开始================params:{}",params);
        if("123".length()==31){
            LOGGER.info("inventory=========testTcc try 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("inventory try 操作 模拟确认提交报错123");
        }
        return Boolean.TRUE;
    }

    @Hmily(confirmMethod = "confirm", cancelMethod = "cancel")
    @Override
    public boolean testTCCFeginInventoryTryException(Map<String, Object> params) {
        LOGGER.info("inventory=========testTCCFeginInventoryTryException 操作开始================params:{}",params);
        if("123".length()==3){
            LOGGER.info("inventory=========testTCCFeginInventoryTryException try 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("inventory testTCCFeginInventoryTryException try 操作 模拟确认提交报错123");
        }
        return Boolean.TRUE;
    }

    public boolean confirm(Map<String, Object> params) {
        LOGGER.info("inventory=========testTcc confirm 操作开始================params:{}",params);

        LOGGER.info("inventory=========testTcc confirm 操作完成================params:{}",params);
        return true;
    }
    public boolean cancel(Map<String, Object> params) {
        LOGGER.info("inventory=========testTcc cancel 操作开始================params:{}",params);
        if("123".length()==31){
            if(!params.containsKey("failNumInventory")){
                params.put("failNumInventory",1);
            }else{
                int failNum = Integer.parseInt(params.get("failNumInventory").toString());
                System.out.println("inventory before----------------------failNumInventory:"+failNum+",--params:"+params);
                failNum +=1;
                params.put("failNumInventory",failNum);
                System.out.println("inventory after----------------------failNumInventory:"+failNum+",--params:"+params);
                if (failNum>3){
//                    LOGGER.info("account====直接返回=====cancelTestTCC 操作开始================params:{}",params);
//                    return false;
                }
            }
            //这样确认方法执行抛出异常时 会再调用一次这个方法 直到确认方法没有报错
            LOGGER.info("inventory =========testTcc cancelTestTCC 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("inventory cancelTestTCC 操作 模拟确认提交报错123");
        }
        LOGGER.info("inventory=========testTcc cancel 操作完成================params:{}",params);
        return true;
    }
}
