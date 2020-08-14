package com.mepeng.cn.SevenPen.order.controller;

import com.mepeng.cn.SevenPen.order.service.DemoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    private DemoService demoService;

    @ResponseBody
    @GetMapping(value = "/demoLocalSQL")
    @ApiOperation(value = "模拟测试myBatis 动态SQL查询")
    public Map<String,Object> demoLocalSQL(@RequestParam(value = "name") String name,@RequestParam Map<String,Object> params) {
        String uuid = java.util.UUID.randomUUID().toString();
        Map<String,Object> result = new HashMap<>();
        result.put("uuid",uuid);
        result.put("name",name);
        List<Map<String, Object>> list = demoService.demoLocalSQL(params);
        result.put("list",list);
        return result;
    }
    @GetMapping(value = "/demoLocalTransaction1")
    @ApiOperation(value = "demoLocalTransaction(本地测试 增加事务标签)")
    public String demoLocalTransaction1(@RequestParam(value = "name") String name) {
        String uuid = java.util.UUID.randomUUID().toString();
        demoService.testLocalTransaction1(uuid,name);
        return "success2";
    }
    @GetMapping(value = "/demoGetTest1")
    @ApiOperation(value = "模拟测试订单调用try最后抛出异常 账户服务库存服务cancelMethod 会执行成功 ")
    public String demoGetTest1(@RequestParam Map<String, Object> params) {
        demoService.testTCC(params);
        return "success1";
    }

    @GetMapping(value = "/demoGetTest2")
    @ApiOperation(value = "模拟测试订单调用fegin 账户服务try抛出异常")
    public String demoGetTest2(@RequestParam Map<String, Object> params) {
        demoService.testTCCFeginAccountTryException(params);
        return "success2";
    }

    @GetMapping(value = "/demoGetTest3")
    @ApiOperation(value = "模拟测试订单调用fegin 库存服务try抛出异常")
    public String demoGetTest3(@RequestParam Map<String, Object> params) {
        demoService.testTCCFeginInventoryTryException(params);
        return "success3";
    }

}

