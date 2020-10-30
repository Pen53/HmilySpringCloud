package com.mepeng.cn.SevenPen.order.excel.controller;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *漏桶限流
 */
@Component
@Data
@Slf4j
public class LeakyBucket {
    /**
     * 读取配置 设置static成员变量
     */
    //@Value("${bucketsize}")
//    public void setBucketsize(String bucketsize){
//        System.out.println("LeakyBucket 6666 bucketsize:"+bucketsize);
//        capacity = Integer.valueOf(bucketsize);
//    }
    // 桶的容量
    public static int capacity = 25;
    // 木桶剩余的水滴的量(初始化的时候的空的桶)
    public static AtomicInteger water =new AtomicInteger(0);
    public LeakyBucket(){

    }
    public LeakyBucket(int size) {
        capacity =size;
    }

    public boolean acquire() {
        // 如果是空桶，水滴数量+1，判断桶容量是否为0，是返回false，否则返回true
        if (water.get() == 0) {
            System.out.println(Thread.currentThread().getName()+" -----没水 capacity:"+capacity);
            return capacity == 0 ? false : true;
        }
        int g = water.get();
        //若不为空桶，尝试加水，判断桶是否装满
        if (g < capacity) {
            System.out.println(Thread.currentThread().getName()+" -----没满，允许加水 g:"+g);
            //没满，允许加水
            return true;
        } else {
            System.out.println(Thread.currentThread().getName()+" -----水满，拒绝加水 g:"+g);
            // 水满，拒绝加水
            return false;
        }
    }
    public void  increase(){
        //加水
        int g = water.incrementAndGet() ;
        System.out.println(Thread.currentThread().getName()+" -----加水 g:"+g);
    }


    public void  decrease(){
        //释放水滴
        int g = water.decrementAndGet() ;
        System.out.println(Thread.currentThread().getName()+" -----释放水滴 g:"+g);
    }
}