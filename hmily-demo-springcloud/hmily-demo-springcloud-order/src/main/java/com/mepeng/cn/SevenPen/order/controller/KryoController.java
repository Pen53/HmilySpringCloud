package com.mepeng.cn.SevenPen.order.controller;

import com.mepeng.cn.SevenPen.order.kryo.Dto.DemoEntity;
import com.mepeng.cn.SevenPen.order.kryo.Dto.ParamsDto;
import com.mepeng.cn.SevenPen.order.kryo.Dto.SubTestSerialization;
import com.mepeng.cn.SevenPen.order.kryo.Dto.TestSerialization;
import com.mepeng.cn.SevenPen.order.kryo.KryoSerializerUtil;
//import org.dromara.hmily.common.serializer.KryoSerializer;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.dromara.hmily.core.helper.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/kryo")
public class KryoController {
    @Autowired
    private KryoSerializerUtil kryoSerializer;

    @RequestMapping("/test")
    public String test(){
        return "test";
    }

    public String demoInfo(String name,Integer age){
        return "demo:"+name+",age:"+age;
    }

    @ResponseBody
    @RequestMapping("/test1")
    public TestSerialization kayoSerializer(){
        System.out.println("test threadId:"+Thread.currentThread().getId()+",name:"+Thread.currentThread().getName()+",KryoSerializerComponent ThreadLocal initialValue is dong ");
        byte[] bytes =new byte[200];
        String[] strings = {"s","s1"};
        System.out.println(Arrays.toString(bytes));
        TestSerialization testSerialization = new TestSerialization();
        testSerialization.setText("aaaaasdwe");
        testSerialization.setName("f");
        testSerialization.setId(999L);
        testSerialization.setFlag(false);
        /**
         * Arrays.asList(strings) 会使反序列化报下面的错误，所以序列化的类都要有无参数构造函数
         * com.esotericsoftware.kryo.KryoException: Class cannot be created (missing no-arg constructor): java.util.Arrays$ArrayList
         */
        //testSerialization.setList(Arrays.asList(strings));
        List<SubTestSerialization> subList = new ArrayList<SubTestSerialization>();
        testSerialization.setList(subList);
        for(int i=0;i<3;i++){
            SubTestSerialization subTestSerialization = new SubTestSerialization();
            subTestSerialization.setName("test_"+i);
            subList.add(subTestSerialization);
        }
        SubTestSerialization subTestSerialization = new SubTestSerialization();
        subTestSerialization.setName("test");
        testSerialization.setSubTestSerialization(subTestSerialization);

        bytes = kryoSerializer.serialize(testSerialization);
        System.out.println("=====================================");
        System.out.println(testSerialization.toString());
        System.out.println("===序列化====testSerialization:"+testSerialization);
        System.out.println("序列化字符串:"+Arrays.toString(bytes));
        System.out.println("=====================================");
        System.out.println("================反序列化=====================");
        TestSerialization testSerialization1 = kryoSerializer.deSerialize(bytes,TestSerialization.class);

        System.out.println(testSerialization1.toString());
        System.out.println("===反序列化====testSerialization1:"+testSerialization1);
        System.out.println("反序列化字符串:"+Arrays.toString(bytes));
        System.out.println("================反序列化over=====================");
        return testSerialization1;
    }
    @RequestMapping("/demo1")
    public void demo1(@RequestParam(value = "serializer") String serializer){
        System.out.println("demo1 threadId:"+Thread.currentThread().getId()+",name:"+Thread.currentThread().getName()+",KryoSerializerComponent ThreadLocal initialValue is dong ");
        System.out.println("================反序列化=====================");
        byte[] bytes = serializer.getBytes();
        TestSerialization testSerialization1 = kryoSerializer.deSerialize(bytes,TestSerialization.class);

        System.out.println(testSerialization1.toString());
        System.out.println("反序列化字符串:"+Arrays.toString(bytes));
        System.out.println("================反序列化over=====================");
    }

    @RequestMapping("/test2")
    public void kayoSerializer2(){
        System.out.println("test threadId:"+Thread.currentThread().getId()+",name:"+Thread.currentThread().getName()+",KryoSerializerComponent ThreadLocal initialValue is dong ");
        byte[] bytes =new byte[200];
        String[] strings = {"s","s1"};
        System.out.println(Arrays.toString(bytes));
        DemoEntity demoEntity = new DemoEntity();
        demoEntity.setName("f");
        List<SubTestSerialization> subList = new ArrayList<SubTestSerialization>();
        demoEntity.setList(subList);
        for(int i=0;i<3;i++){
            SubTestSerialization subTestSerialization = new SubTestSerialization();
            subTestSerialization.setName("test_"+i);
            subList.add(subTestSerialization);
        }

        org.dromara.hmily.common.serializer.KryoSerializer k1 = new org.dromara.hmily.common.serializer.KryoSerializer();
        bytes = k1.serialize(demoEntity);
        System.out.println("=====================================");
        System.out.println(demoEntity.toString());
        System.out.println("===序列化====testSerialization:"+demoEntity);
        System.out.println("序列化字符串:"+Arrays.toString(bytes));
        System.out.println("=====================================");
        System.out.println("================反序列化=====================");
        DemoEntity demoEntity1 = k1.deSerialize(bytes,DemoEntity.class);

        System.out.println(demoEntity1.toString());
        System.out.println("===反序列化====demoEntity1:"+demoEntity1);
        System.out.println("反序列化字符串:"+Arrays.toString(bytes));
        System.out.println("================反序列化over=====================");


        //保存参数值
        /*Object[] paramsValues = new Object[parameterTypes.length];
        Method method = null;
        try {
            String beanName = lowerFirstCase(method.getDeclaringClass().getSimpleName());
            //利用反射机制来调用
            method.invoke(this.ioc.get(beanName), paramsValues);
        }catch (Exception e){
            e.printStackTrace();
        }*/
        KryoController kryoController =  SpringBeanUtils.getInstance().getBean(KryoController.class);
        System.out.println("kryoController:"+kryoController);

        String className = kryoController.getClass().getSimpleName();
        System.out.println("kryoController:"+kryoController+",className:"+className );
        System.out.println("cns:"+className.substring(1,className.length()));
        String s1 = firstLowercase(className);
        System.out.println("cns s1:"+s1);
        String methodName = "demoInfo";

        Method[] methods = kryoController.getClass().getMethods();
        String name = "demoInfo";
        Class<?>[] parameterTypes = new Class[]{String.class,Integer.class};
        try {
            System.out.println("next ------name:"+name);
            Method method = kryoController.getClass().getMethod(name, parameterTypes);
            System.out.println("name:"+name+",method:"+method);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        Object[] params = new Object[parameterTypes.length];
        params[0] = "彭大虾";
        params[1] = 20;
        bytes = kryoSerializer.serialize(params);

        if(methods.length>0){
            for(Method method:methods){
                if(method.getName().equals(methodName)){
                    method.setAccessible(true);
                    Object[] args = null;
                    try {
                        Object result = method.invoke(kryoController, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String firstLowercase(String target) {
        if (StringUtils.isEmpty(target)) {
            return target;
        } else {
            char[] targetChar = target.toCharArray();
            targetChar[0] = (char)(targetChar[0] + 32);
            return String.valueOf(targetChar);
        }
    }

    private static String firstDelete(String target) {
        return StringUtils.isEmpty(target) ? target : target.substring(1, target.length());
    }

    @RequestMapping("/test3")
    @ApiOperation(value = "模拟hmily 回调方法（调用失败，继续调用相关方法）")
    public Object kayoSerializer3(@RequestParam(value = "name") String name,
                                  @RequestParam(value = "age") Integer age){
        ParamsDto paramsDto = new ParamsDto();
        paramsDto.setCls(KryoController.class);//调用方法的类
        String methodName = "demoInfo";//调用方法名
        paramsDto.setMethodName(methodName);
        Class<?>[] parameterTypes = new Class[]{String.class,Integer.class};
        Object[] args = new Object[parameterTypes.length];
        args[0] = "彭大虾";
        args[1] = 20;
        paramsDto.setArgs(args);//调用方法参数值
        paramsDto.setParameterTypes(parameterTypes);//调用方法参数列表
        byte[] bytes = kryoSerializer.serialize(paramsDto);

        System.out.println("===序列化====paramsDto:"+paramsDto);
        System.out.println("序列化字符串:"+Arrays.toString(bytes));
        System.out.println("=====================================");
        System.out.println("================反序列化=====================");
        ParamsDto paramsDto1 = kryoSerializer.deSerialize(bytes,ParamsDto.class);

        System.out.println(paramsDto1.toString());
        System.out.println("===反序列化====demoEntity1:"+paramsDto1);
        System.out.println("================反序列化over=====================");

        Object kryoController = SpringBeanUtils.getInstance().getBean(paramsDto1.getCls());

        System.out.println("kryoController:"+kryoController);
        Method[] methods = kryoController.getClass().getMethods();
        for(Method method:methods){
            Class<?>[] types = method.getParameterTypes();
            if(method.getName().equals(methodName)){
                System.out.println("types:"+types);
                System.out.println("getParameterTypes:"+paramsDto1.getParameterTypes());
                System.out.println("getArgs:"+paramsDto1.getArgs());
            }
        }
        try {
            Method method = kryoController.getClass().getMethod(paramsDto1.getMethodName(), paramsDto1.getParameterTypes());
            System.out.println("name:"+paramsDto1.getMethodName()+",method:"+method);
            if(method==null){
                throw new RuntimeException("classs:"+paramsDto1.getCls().toString()+",name:"+paramsDto1.getMethodName()+"方法找不到");
            }
            method.setAccessible(true);
            Object result = method.invoke(kryoController,paramsDto1.getArgs());
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
