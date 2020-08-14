package com.mepeng.cn.SevenPen.order.util;

public class Demo {
    public static void main(String[] args) {
        Class<A> aCls = A.class;
        Class<B> bCls = B.class;
        System.out.println("a is b parent"+aCls.isAssignableFrom(bCls));

        Class<AInterImpl> aiCls = AInterImpl.class;
        Class<AInter> aiCls0 = AInter.class;

        System.out.println("aiCls0 is aiCls parent:"+aiCls0.isAssignableFrom(aiCls));
    }
}
