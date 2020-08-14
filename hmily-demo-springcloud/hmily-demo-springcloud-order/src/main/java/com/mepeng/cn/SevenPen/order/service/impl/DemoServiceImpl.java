package com.mepeng.cn.SevenPen.order.service.impl;

import com.mepeng.cn.SevenPen.order.annotation.PenHmily;
import com.mepeng.cn.SevenPen.order.controller.KryoController;
import com.mepeng.cn.SevenPen.order.kryo.Dto.ParamsDto;
import com.mepeng.cn.SevenPen.order.kryo.Dto.PenHimlyEntity;
import com.mepeng.cn.SevenPen.order.util.ObjectByteUtil;
import org.dromara.hmily.annotation.Hmily;
import org.dromara.hmily.common.exception.HmilyRuntimeException;
import org.dromara.hmily.common.utils.IdWorkerUtils;
import com.mepeng.cn.SevenPen.order.client.AccountClient;
import com.mepeng.cn.SevenPen.order.client.InventoryClient;
import com.mepeng.cn.SevenPen.order.entity.Demo;
import com.mepeng.cn.SevenPen.order.entity.Order;
import com.mepeng.cn.SevenPen.order.entity.Test;
import com.mepeng.cn.SevenPen.order.enums.OrderStatusEnum;
import com.mepeng.cn.SevenPen.order.mapper.DemoMapper;
import com.mepeng.cn.SevenPen.order.mapper.OrderMapper;
import com.mepeng.cn.SevenPen.order.mapper.TestMapper;
import com.mepeng.cn.SevenPen.order.service.DemoService;
import org.dromara.hmily.core.helper.SpringBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;


@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private AccountClient accountClient;

    @Autowired
    private InventoryClient inventoryClient;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private DemoMapper demoMapper;
    @Autowired
    private TestMapper testMapper;
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);

    /**
     * tcc 事务测试 默认抛出异常是不会回滚数据的
     * @param name
     * @param uuid
     */
    @Hmily(confirmMethod = "confirmLocalTransaction1", cancelMethod = "cancelLocalTransaction1")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testLocalTransaction1(String uuid,String name) {

        Demo demo = new Demo();
        demo.setName(name);
        demo.setCreateTime(new Date());
        demo.setStatus(0);
        demo.setUuid(uuid);
        demoMapper.insert(demo);
        System.out.println("demo id:"+demo.getId());
        Test test = new Test();
        test.setName(name);
        test.setCreateTime(new Date());
        test.setStatus(0);
        test.setUuid(uuid);
        testMapper.insert(test);
        System.out.println("test id:"+test.getId());
        if("csf1".equals(name)){
            throw new HmilyRuntimeException("Name is csf1 抛出异常");
        }
        if("csf2".equals(name)){
            throw new HmilyRuntimeException("Name is csf2 抛出异常");
        }
        if("csf3".equals(name)){
            throw new HmilyRuntimeException("Name is csf3抛出异常");
        }
        if("csf4".equals(name)){
            throw new HmilyRuntimeException("Name is csf4 抛出异常");
        }
    }
    @Transactional
    public void confirmLocalTransaction1(String uuid,String name) {
        LOGGER.info("=========testLocalTransaction confirmLocalTransaction1 cancel操作开始================uuid:"+uuid+",name:"+name);
        Demo demo = demoMapper.selectByUuid(uuid);
        if(demo!=null){
            demo.setStatus(1);
            int affect = demoMapper.update(demo);
            System.out.println("confirmLocalTransaction1 demo setStatus 1 affect:"+affect);
        }

        Test test = testMapper.selectByUuid(uuid);
        if(test!=null){
            test.setStatus(1);
            int affect = testMapper.update(test);
            System.out.println("confirmLocalTransaction1 test setStatus 1 affect:"+affect);
        }
        //orderMapper.update(order);
        LOGGER.info("=========testLocalTransaction confirmLocalTransaction1 confirm操作完成================");
    }
    @Transactional
    public void cancelLocalTransaction1(String uuid,String name) {
        LOGGER.info("=========testLocalTransaction cancelLocalTransaction1 cancel操作开始================uuid:"+uuid+",name:"+name);
        Demo demo = demoMapper.selectByUuid(uuid);
        if(demo!=null){
            demo.setStatus(2);
            int affect = demoMapper.update(demo);
            System.out.println("cancelLocalTransaction1 demo setStatus 2 affect:"+affect);
        }

        Test test = testMapper.selectByUuid(uuid);
        if(test!=null){
            test.setStatus(2);
            int affect = testMapper.update(test);
            System.out.println("cancelLocalTransaction1 test setStatus 2 affect:"+affect);
        }
        LOGGER.info("=========testLocalTransaction cancelLocalTransaction1 cancel操作完成================");
    }

    /**
     * Hmily 分布式事务
     * 当 try 阶段是发送异常时，其他调用的fegin接口，也用了Hmily的会感知到，同时调用 cancelMethod 方法
     * 自己也会调用 cancelMethod 方法
     * @param params
     */
    @Hmily(confirmMethod = "confirmTestTCC", cancelMethod = "cancelTestTCC")
    @Override
    public void testTCC(Map<String,Object> params) {
        LOGGER.info("=========testTCC 操作开始================params:{}",params);
        LOGGER.info("===========Next accountClient.testTcc  ==============params:"+params);
        try {
            boolean result = accountClient.testTcc(params);
            System.out.println("Next accountClient.testTcc result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next accountClient.testTcc  ==============Exception e:{}",e);
            throw e;
        }
        LOGGER.info("===========Next inventoryClient.testTcc  ==============params:"+params);
        try {
            boolean result = inventoryClient.testTcc(params);
            System.out.println("Next inventoryClient.testTcc result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next inventoryClient.testTcc  ==============Exception e:{}",e);
            throw e;
        }
        if("123".length()==3){
            LOGGER.info("=========testTcc try 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("try 操作 模拟确认提交报错123");
        }
        LOGGER.info("=========testTCC 操作完成================");
    }



    /**
     * 只要此方法执行失败，会一直调用这个方法 用原来的参数，参数可以动态改变
     * 注意只要执行成功，不报错，那么这个方法相应参数 就不会执行了
     * @param params
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmTestTCC(Map<String,Object> params) {
        LOGGER.info("=========confirmTestTCC 操作开始================params:{}",params);
        if("123".length()==1){
            //这样确认方法执行抛出异常时 会再调用一次这个方法 直到确认方法没有报错
            LOGGER.info("=========testTcc confirmTestTCC 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("操作 模拟确认提交报错123");
        }
        LOGGER.info("=========confirmTestTCC 操作完成================");
    }
    /**
     * 只要此方法执行失败，会一直调用这个方法 用原来的参数，参数可以动态改变
     * 注意只要执行成功，不报错，那么这个方法相应参数 就不会执行了
     * @param params
     */
    public void cancelTestTCC(Map<String,Object> params) {
        LOGGER.info("=========cancelTestTCC 操作开始================params:{}",params);

        if("123".length()==3){
            if(!params.containsKey("failNum")){
                params.put("failNum",1);
            }else{
                int failNum = Integer.parseInt(params.get("failNum").toString());
                System.out.println("before----------------------failNum:"+failNum+",--params:"+params);
                failNum +=1;
                params.put("failNum",failNum);
                System.out.println("after----------------------failNum:"+failNum+",--params:"+params);
                if (failNum>3){
                    LOGGER.info("====直接返回=====cancelTestTCC 操作开始================params:{}",params);
                    return ;
                }
            }

            //这样确认方法执行抛出异常时 会再调用一次这个方法 直到确认方法没有报错
            LOGGER.info("=========testTcc cancelTestTCC 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("cancelTestTCC 操作 模拟确认提交报错123");
        }
        LOGGER.info("=========cancelTestTCC 操作完成================");
    }

    /**
     * mybatis 动态SQL Demo
     * @return
     */
    @Override
    public List<Map<String, Object>> demoLocalSQL(Map<String,Object> params) {
        Map<String,Object> map = new HashMap<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select * from tcc_demo where name like concat('%', #{name},'%')");
        map.put("sql",sql.toString());
        map.put("name",params.get("name"));
        List<Map<String, Object>>  list = demoMapper.demoLocalSQL(map);
        return  list;
    }

    @Hmily(confirmMethod = "testTCCFeginAccountTryExceptionConfirm", cancelMethod = "testTCCFeginAccountTryExceptionCancel")
    @Override
    public void testTCCFeginAccountTryException(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginAccountTryException 操作开始================params:{}",params);
        LOGGER.info("===========Next accountClient.testTCCFeginAccountTryException  ==============params:"+params);
        try {
            boolean result = accountClient.testTCCFeginAccountTryException(params);
            System.out.println("Next accountClient.testTCCFeginAccountTryException result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next accountClient.testTCCFeginAccountTryException  ==============Exception e:{}",e);
            throw e;
        }
        LOGGER.info("===========Next inventoryClient.testTcc  ==============params:"+params);
        try {
            boolean result = inventoryClient.testTcc(params);
            System.out.println("Next inventoryClient.testTcc result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next inventoryClient.testTcc  ==============Exception e:{}",e);
            throw e;
        }
        if("123".length()==3){
            LOGGER.info("=========testTCCFeginAccountTryExceptionConfirm try 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("testTCCFeginAccountTryExceptionConfirm 操作 模拟确认提交报错123");
        }
        LOGGER.info("=========testTCCFeginAccountTryExceptionConfirm 操作完成================");
    }



    public void testTCCFeginAccountTryExceptionConfirm(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginAccountTryExceptionConfirm 操作开始================params:{}",params);

        LOGGER.info("=========testTCCFeginAccountTryExceptionConfirm 操作完成================");
    }
    public void testTCCFeginAccountTryExceptionCancel(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginAccountTryExceptionCancel 操作开始================params:{}",params);

        LOGGER.info("=========testTCCFeginAccountTryExceptionCancel 操作完成================");
    }

    @Hmily(confirmMethod = "testTCCFeginInventoryTryExceptionConfirm", cancelMethod = "testTCCFeginInventoryTryExceptionCancel")
    @Override
    public void testTCCFeginInventoryTryException(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginInventoryTryException 操作开始================params:{}",params);
        LOGGER.info("===========Next accountClient.testTcc  ==============params:"+params);
        try {
            boolean result = accountClient.testTcc(params);
            System.out.println("Next accountClient.testTcc result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next accountClient.testTcc  ==============Exception e:{}",e);
            throw e;
        }
        LOGGER.info("===========Next inventoryClient.testTCCFeginInventoryTryException  ==============params:"+params);
        try {
            boolean result = inventoryClient.testTCCFeginInventoryTryException(params);
            System.out.println("Next inventoryClient.testTCCFeginInventoryTryException result:"+result);
        }catch (Exception e){
            LOGGER.info("===========Next inventoryClient.testTCCFeginInventoryTryException  ==============Exception e:{}",e);
            throw e;
        }
        if("123".length()==1){
            LOGGER.info("=========testTCCFeginInventoryTryException try 操作 模拟确认提交报错123================params:{}",params);
            //throw new RuntimeException("模拟确认提交报错123");
            throw new HmilyRuntimeException("testTCCFeginInventoryTryException 操作 模拟确认提交报错123");
        }
        LOGGER.info("=========testTCCFeginInventoryTryException 操作完成================");
    }

    @PenHmily(confirmMethod = "testPenHmilyConfirm",cancelMethod = "testPenHmilyCancel")
    @Override
    public void testPenHmily(String name, Map<String, Object> params) {
        System.out.println("testPenHmily is doing name:"+name+",params:"+params);
        if(name.equals("csf234")){
            throw new RuntimeException("test testPenHmily transaction123 csf");
        }
        if(name.equals("csf2")){
            throw new RuntimeException("test testPenHmily transaction123 csf2");
        }
    }

    @Override
    public PenHimlyEntity penHimlyDemo(String name, Map<String, Object> params) {
        PenHimlyEntity demo = demoMapper.selectPenHimlyEntityById(1L);
        byte[] data = demo.getData();
        try {
            Object obj = ObjectByteUtil.byteToObject(data);
            if(obj instanceof ParamsDto){
                ParamsDto dto = (ParamsDto)obj;
                String clsName = demo.getTargetClass();
                Class<?> cls = Class.forName(clsName);

                String methodName = demo.getTargetMethod();
                Object bean = SpringBeanUtils.getInstance().getBean(cls);
                Method method = cls.getMethod(methodName, dto.getParameterTypes());

                Object result = method.invoke(bean, dto.getArgs());

                System.out.println("clsName:"+clsName+",methodName:"+methodName+",执行结果result:"+result);
                System.out.println("args:"+ Arrays.toString(dto.getArgs()));
            }

            System.out.println("obj:"+obj);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  demo ;
    }

    public void testPenHmilyConfirm(String name, Map<String, Object> params) {
        System.out.println("testPenHmilyConfirm is doing name:"+name+",params:"+params);
        if(name.equals("csf2")){
            throw new RuntimeException("test testPenHmilyConfirm transaction123 csf2");
        }
    }
    public void testPenHmilyCancel(String name, Map<String, Object> params) {
        System.out.println("testPenHmilyCancel is doing name:"+name+",params:"+params);
        if(name.equals("csf3")){
            throw new RuntimeException("test testPenHmilyCancel transaction123 csf3");
        }
    }

    public void testTCCFeginInventoryTryExceptionConfirm(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginInventoryTryExceptionConfirm 操作开始================params:{}",params);

        LOGGER.info("=========testTCCFeginInventoryTryExceptionConfirm 操作完成================");
    }
    public void testTCCFeginInventoryTryExceptionCancel(Map<String, Object> params) {
        LOGGER.info("=========testTCCFeginInventoryTryExceptionCancel 操作开始================params:{}",params);

        LOGGER.info("=========testTCCFeginInventoryTryExceptionCancel 操作完成================");
    }
}
