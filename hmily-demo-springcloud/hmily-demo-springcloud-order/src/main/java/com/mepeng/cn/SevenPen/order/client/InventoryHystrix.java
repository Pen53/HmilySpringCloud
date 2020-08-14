package com.mepeng.cn.SevenPen.order.client;

import com.mepeng.cn.SevenPen.order.dto.InventoryDTO;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaoyu(Myth)
 */
@Component
public class InventoryHystrix implements InventoryClient {


    @Override
    public Boolean decrease(InventoryDTO inventoryDTO) {
        System.out.println("inventory hystrix.......");
        return false;
    }

    @Override
    public Integer findByProductId(String productId) {
        return 0;
    }

    @Override
    public Boolean mockWithTryException(InventoryDTO inventoryDTO) {
        return false;
    }

    @Override
    public Boolean mockWithTryTimeout(InventoryDTO inventoryDTO) {
        return false;
    }

    @Override
    public Boolean testTcc(Map<String, Object> params) {
        System.out.println("InventoryHystrix testTcc 执行断路器。。params:" + params);
        return false;
    }

    @Override
    public Boolean testTCCFeginInventoryTryException(Map<String, Object> params) {
        System.out.println("InventoryHystrix testTCCFeginInventoryTryException 执行断路器。。params:" + params);
        return false;
    }
}
