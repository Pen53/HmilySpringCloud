package com.mepeng.cn.SevenPen.order.excel;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelDefine {
    int value();

    int startRow() default 1;
}
