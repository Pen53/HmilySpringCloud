package com.mepeng.cn.SevenPen.order.client;

import org.dromara.hmily.annotation.Hmily;
import com.mepeng.cn.SevenPen.order.dto.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * The interface Account client.
 *
 * @author xiaoyu
 */
@FeignClient(value = "account-service")
public interface AccountClient {

    /**
     * 用户账户付款.
     *
     * @param accountDO 实体类
     * @return true 成功
     */
    @RequestMapping("/account-service/account/payment")
    @Hmily
    Boolean payment(@RequestBody AccountDTO accountDO);


    /**
     * 获取用户账户信息.
     *
     * @param userId 用户id
     * @return AccountDO big decimal
     */
    @RequestMapping("/account-service/account/findByUserId")
    BigDecimal findByUserId(@RequestParam("userId") String userId);

    /**
     * 测试 TCC 事务
     *
     * @param params 请求参数
     * @return true 成功
     */
    @RequestMapping("/account-service/account/testTcc")
    @Hmily
    Boolean testTcc(@RequestBody Map<String, Object> params);

    @RequestMapping("/account-service/account/testTCCFeginAccountTryException")
    @Hmily
    Boolean testTCCFeginAccountTryException(Map<String, Object> params);
}
