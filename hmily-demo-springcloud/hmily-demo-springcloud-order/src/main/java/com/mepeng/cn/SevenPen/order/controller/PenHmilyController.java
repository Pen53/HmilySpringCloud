package com.mepeng.cn.SevenPen.order.controller;

import com.mepeng.cn.SevenPen.order.annotation.PenHmily;
import com.mepeng.cn.SevenPen.order.kryo.KryoSerializerUtil;
import com.mepeng.cn.SevenPen.order.service.DemoService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/penHmily")
public class PenHmilyController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PenHmilyController.class);
    @Autowired
    private DemoService demoService;

    @Autowired
    private KryoSerializerUtil kryoSerializer;

    @ResponseBody
    @RequestMapping("/test")
    public String test(@RequestParam(value = "name") String name,@RequestParam Map<String,Object> params){
        LOGGER.debug("PenHmilyController test params:"+params);
        demoService.testPenHmily(name,params);
        return "test";
    }

    @PenHmily
    @ResponseBody
    @RequestMapping("/test1")
    public String test1(@RequestParam(value = "name") String name,@RequestParam Map<String,Object> params){
        System.out.println("PenHmilyController test1 params:"+params);
        LOGGER.debug("PenHmilyController test1 params:"+params);
        LOGGER.info("PenHmilyController test1233 params:"+params);
        //demoService.testPenHmily(name,params);
        return "test1";
    }

    @PenHmily
    @ResponseBody
    @RequestMapping("/penHimlyDemo")
    @ApiOperation(value = "模拟hmily报错，报错数据库，再查询数据库信息，重新执行方法")
    public String penHimlyDemo(@RequestParam(value = "name") String name,@RequestParam Map<String,Object> params){
        System.out.println("PenHmilyController penHimlyDemo params:"+params);
        LOGGER.debug("PenHmilyController penHimlyDemo params:"+params);
        demoService.penHimlyDemo(name,params);
        return "penHimlyDemo";
    }

    public static void main(String[] args) {
        Class<Map> cls = Map.class;

        Class<HashMap> hcls = HashMap.class;
        boolean flag = cls.isAssignableFrom(hcls);
        System.out.println("flag:"+flag);
    }

}
