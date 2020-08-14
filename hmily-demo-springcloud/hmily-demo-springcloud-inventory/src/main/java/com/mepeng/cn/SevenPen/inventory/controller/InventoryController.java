/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mepeng.cn.SevenPen.inventory.controller;

import com.mepeng.cn.SevenPen.inventory.dto.InventoryDTO;
import com.mepeng.cn.SevenPen.inventory.service.DemoService;
import com.mepeng.cn.SevenPen.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author xiaoyu
 */
@RestController
@RequestMapping("/inventory")
@SuppressWarnings("all")
public class InventoryController {

    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @RequestMapping("/decrease")
    public Boolean decrease(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.decrease(inventoryDTO);
    }

    @RequestMapping("/findByProductId")
    public Integer findByProductId(@RequestParam("productId") String productId) {
        return inventoryService.findByProductId(productId).getTotalInventory();
    }

    @RequestMapping("/mockWithTryException")
    public Boolean mockWithTryException(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.mockWithTryException(inventoryDTO);
    }

    @RequestMapping("/mockWithTryTimeout")
    public Boolean mockWithTryTimeout(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.mockWithTryTimeout(inventoryDTO);
    }

    @Autowired
    private DemoService demoService;
    @RequestMapping("/testTcc")
    public Boolean testTcc(@RequestBody Map<String,Object> params) {
        System.out.println("InventoryController testTcc is doing params:"+params);
        return demoService.testTcc(params);
    }

    @RequestMapping("/testTCCFeginInventoryTryException")
    public Boolean testTCCFeginInventoryTryException(@RequestBody Map<String,Object> params) {
        System.out.println("InventoryController testTCCFeginInventoryTryException is doing params:"+params);
        return demoService.testTCCFeginInventoryTryException(params);
    }

}
