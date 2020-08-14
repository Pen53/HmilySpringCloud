package com.mepeng.cn.SevenPen.order.service;

import com.mepeng.cn.SevenPen.order.kryo.Dto.PenHimlyEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Demo 2020-08-05
 */
public  interface DemoService {
    void testTCC(Map<String, Object> params);

    void testLocalTransaction1(String uuid, String name);


    List<Map<String, Object>> demoLocalSQL(Map<String,Object> params);

    void testTCCFeginAccountTryException(Map<String, Object> params);

    void testTCCFeginInventoryTryException(Map<String, Object> params);

    void testPenHmily(String name,Map<String, Object> params);

    PenHimlyEntity penHimlyDemo(String name, Map<String, Object> params);
}
