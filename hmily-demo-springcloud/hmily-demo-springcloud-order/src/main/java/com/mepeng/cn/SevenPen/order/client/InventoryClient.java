package com.mepeng.cn.SevenPen.order.client;

import com.mepeng.cn.SevenPen.order.dto.InventoryDTO;
import org.dromara.hmily.annotation.Hmily;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * The interface Inventory client.
 *
 * @author xiaoyu
 */
@FeignClient(value = "inventory-service")
public interface InventoryClient {

    /**
     * 库存扣减.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @RequestMapping("/inventory-service/inventory/decrease")
    @Hmily
    Boolean decrease(@RequestBody InventoryDTO inventoryDTO);


    /**
     * 获取商品库存.
     *
     * @param productId 商品id
     * @return InventoryDO integer
     */
    @RequestMapping("/inventory-service/inventory/findByProductId")
    Integer findByProductId(@RequestParam("productId") String productId);


    /**
     * 模拟库存扣减异常.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @Hmily
    @RequestMapping("/inventory-service/inventory/mockWithTryException")
    Boolean mockWithTryException(@RequestBody InventoryDTO inventoryDTO);


    /**
     * 模拟库存扣减超时.
     *
     * @param inventoryDTO 实体对象
     * @return true 成功
     */
    @Hmily
    @RequestMapping("/inventory-service/inventory/mockWithTryTimeout")
    Boolean mockWithTryTimeout(@RequestBody InventoryDTO inventoryDTO);

    /**
     * 测试 TCC 事务
     *
     * @param params 请求参数
     * @return true 成功
     */
    @RequestMapping("/inventory-service/inventory/testTcc")
    @Hmily
    Boolean testTcc(@RequestBody Map<String, Object> params);

    /**
     * 测试 TCC 事务
     *
     * @param params 请求参数
     * @return true 成功
     */
    @RequestMapping("/inventory-service/inventory/testTCCFeginInventoryTryException")
    @Hmily
    Boolean testTCCFeginInventoryTryException(@RequestBody Map<String, Object> params);

}
