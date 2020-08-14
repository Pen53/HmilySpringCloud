package com.mepeng.cn.SevenPen.account.service;

import org.dromara.hmily.annotation.Hmily;

import java.util.Map;

public interface DemoService {
    @Hmily
    Boolean testTcc(Map<String, Object> params);

    Boolean testTCCFeginAccountTryException(Map<String, Object> params);

}
