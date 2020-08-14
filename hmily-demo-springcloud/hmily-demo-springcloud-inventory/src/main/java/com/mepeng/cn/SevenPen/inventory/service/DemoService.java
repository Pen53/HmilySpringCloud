package com.mepeng.cn.SevenPen.inventory.service;

import org.dromara.hmily.annotation.Hmily;

import java.util.Map;

public interface DemoService {
    @Hmily
    boolean testTcc(Map<String, Object> params);

    @Hmily
    boolean testTCCFeginInventoryTryException(Map<String, Object> params);

}
