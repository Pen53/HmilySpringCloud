package com.mepeng.cn.SevenPen.order.kryo.Dto;

import java.lang.reflect.Method;
import java.util.List;

public class DemoEntity {
    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

    private List<?> list;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private String name;

    public static void main1(String[] args) {
        DemoEntity demo = new DemoEntity();
        demo.setName("csf");
        System.out.println("demo:"+demo);

        /*System.out.println(new JSONObject().fromObject(Thread.currentThread()
                .getStackTrace()[2]));*/
        System.out
                .println(Thread.currentThread().getStackTrace()[2].toString());
        System.out.println("--------------");
        String className = Thread.currentThread().getStackTrace()[2]
                .getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2]
                .getMethodName();
        System.out.println(className);
        System.out.println(methodName);
    }

    public static void main(String[] args) {
        test2("1");
    }
    public static void test1() {

        help();
    }

    public static void test2(String a) {
        help();
    }

    private static String help() {
        /*
         *  在运行时通过获取堆栈信息来提取当前方法所处的类路径及当前方法的名称
         */
        /*System.out.println(new JSONObject().fromObject(Thread.currentThread()
                .getStackTrace()[2]));*/
        System.out
                .println(Thread.currentThread().getStackTrace()[2].toString());
        System.out.println("--------------");
        String className = Thread.currentThread().getStackTrace()[2]
                .getClassName();
        String methodName = Thread.currentThread().getStackTrace()[2]
                .getMethodName();
        System.out.println(className);
        System.out.println(methodName);

        StackTraceElement[] sta = Thread.currentThread().getStackTrace();

        /*
         * 但在jvm装载该类的class文件时并不使用"形参"这个概念,形参只是在编码的时候方便程序员记忆的,因此无法通过原生的java api
         * 来获取运行时当前方法的形参名称(仅仅能够知道参数的个数),但可以借助参数注解来存储形参的名称或者其它相关信息,然后再通过
         * 反射来提取这些信息.不过这样感觉就不是那么完美了.
         */
        try {
            Class clazz = Class.forName(className);
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals(methodName)) {
                    System.out.println(method.getParameterAnnotations().length);
                    /*System.out.println(((LogTarget) method
                            .getParameterAnnotations()[0][0]).comment());*/
                    // System.out.println(this);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return className;
    }
}
