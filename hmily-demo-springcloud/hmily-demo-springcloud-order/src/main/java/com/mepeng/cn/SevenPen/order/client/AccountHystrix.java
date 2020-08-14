package com.mepeng.cn.SevenPen.order.client;

import com.mepeng.cn.SevenPen.order.dto.AccountDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The type Account hystrix.
 *
 * @author xiaoyu(Myth)
 */
@Component
public class AccountHystrix implements AccountClient {

    @Override
    public Boolean payment(AccountDTO accountDO) {
        System.out.println("执行断路器。。" + accountDO.toString());
        return false;
    }

    @Override
    public BigDecimal findByUserId(String userId) {
        System.out.println("执行断路器。。");
        return BigDecimal.ZERO;
    }

    @Override
    public Boolean testTcc(Map<String, Object> params) {
        System.out.println("AccountHystrix testTcc 执行断路器。。params:" + params);
        return false;
    }

    @Override
    public Boolean testTCCFeginAccountTryException(Map<String, Object> params) {
        System.out.println("AccountHystrix testTCCFeginAccountTryException 执行断路器。。params:" + params);
        return false;
    }
}
